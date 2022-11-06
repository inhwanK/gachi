package org.deco.gachicoding.file.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.file.domain.File;
import org.deco.gachicoding.file.domain.repository.FileRepository;
import org.deco.gachicoding.file.infrastructure.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

        private final FileRepository fileRepository;

        private final S3Service s3Service;

        @Value("${cloud.aws.s3.url}")
        private String s3Url;

        @Transactional
        public void registerFile(File file) {
                fileRepository.save(file);
        }

        @Transactional
        public void compareFilePathAndDelete(
                Long idx,
                String category,
                List<String> pathInArticle
        ) {
                List<String> removePath = compareFilePath(
                        pathInArticle,
                        fileRepository.findFilePathByCategoryAndIdx(category, idx)
                );

                if (!removePath.isEmpty())
                        removeAll(removePath);

        }

        private List<String> getFilePath(Long idx, String category) {
                return fileRepository.findFilePathByCategoryAndIdx(category, idx);
        }

        private List<String> compareFilePath(List<String> pathInArticle, List<String> pathInDb) {
                List<String> removeQueue = new LinkedList<>();
                for (String path : pathInDb) {
                        if (!pathInArticle.contains(path))
                                removeQueue.add(path);
                }

                return removeQueue;
        }

        // removeAll? disableAll? -> 결정에 따라 달라 짐
        private void removeAll(List<String> path) {

        }

        @Transactional
        public Queue<String> imgProducer(String content) {

                Queue<String> imgQueue = new LinkedList<>();

                // 정규 표현식 공부하자
                Pattern nonValidPattern = Pattern
                        .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");

                Matcher matcher = nonValidPattern.matcher(content);

                while (matcher.find()) {
                        String img = matcher.group(1);
                        imgQueue.add(img.replace(s3Url, ""));
                }

                return imgQueue;
        }

        public String imgConsumer(Long idx, String content, String category, Queue<String> queue) {

                while (!queue.isEmpty()) {
                        String beforeImg = queue.poll();

                        String afterImg = uploadRealImg(idx, beforeImg, category);

                        log.info("beforeImg = " + beforeImg);
                        log.info("afterImg = " + afterImg);
                        content = content.replace(beforeImg, afterImg);
                        log.info(content);
                }

                return content;
        }

        private String uploadRealImg(Long idx, String path, String category) {

                log.info("oldPath : " + path);

                ObjectMetadata objectMetadata = s3Service.getObjectMetadata(path);

                File file = FileAssembler.file(idx, category, objectMetadata);

                registerFile(file);

                String newPath = file.getFilePath();
                log.info("newPath : " + newPath);

                return s3Service.replaceS3(path, newPath);
        }
}
