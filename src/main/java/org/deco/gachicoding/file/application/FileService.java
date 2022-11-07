package org.deco.gachicoding.file.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.file.FileNotFoundException;
import org.deco.gachicoding.file.domain.File;
import org.deco.gachicoding.file.domain.repository.FileRepository;
import org.deco.gachicoding.file.infrastructure.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

        // async
        @Transactional
        public String compareFilePathAndOptimization(
                Long idx,
                String category,
                String content
        ) {

                Queue<String> addedQueue = compareFilePath(
                        imgProducer(content),
                        findFilesByCategoryAndIdx(category, idx)
                );

                return imgConsumer(idx, category, content, addedQueue);
        }

        private Queue<String> compareFilePath(Queue<String> pathInArticle, List<File> pathInDB) {

                for (Iterator<String> pathIter = pathInArticle.iterator(); pathIter.hasNext();) {
                        String path = pathIter.next();
                        log.info("path : {}", path);
                        for (Iterator<File> fileIter = pathInDB.iterator(); fileIter.hasNext();) {
                                File file = fileIter.next();
                                log.info("file : {}", file.getOriginFilename());

                                if (file.compareFilePath(path)) {
                                        log.info("같은 파일이 있네요");
                                        pathIter.remove();
                                        fileIter.remove();
                                }
                        }
                }

                // 추가해야 할 파일 pathInArticle
                // 삭제해야 할 파일 pathInDB
                if (!pathInDB.isEmpty())
                        deleteAll(pathInDB);

                return pathInArticle;

        }

        // removeAll? disableAll? -> 결정에 따라 달라 짐
        private void deleteAll(List<File> removedFiles) {
                fileRepository.deleteAll(removedFiles);
        }

        // async?
        @Transactional
        public String extractPathAndS3Upload(Long idx, String category, String content) {
                return imgConsumer(idx, category, content, imgProducer(content));
        }

        private Queue<String> imgProducer(String content) {

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

        private String imgConsumer(Long idx, String category, String content, Queue<String> queue) {

                while (!queue.isEmpty()) {
                        String beforeImg = queue.poll();

                        String afterImg = uploadRealImg(idx, category, beforeImg);

                        log.info("beforeImg = " + beforeImg);
                        log.info("afterImg = " + afterImg);
                        content = content.replace(beforeImg, afterImg);
                        log.info(content);
                }

                return content;
        }

        private String uploadRealImg(Long idx, String category, String path) {

                log.info("oldPath : " + path);

                ObjectMetadata objectMetadata = s3Service.getObjectMetadata(path);

                File file = FileAssembler.file(idx, category, objectMetadata);

                registerFile(file);

                String newPath = file.getFilePath();
                log.info("newPath : " + newPath);

                s3Service.replaceS3(path, newPath);

                return newPath;
        }

        private List<File> findFilesByCategoryAndIdx(String category, Long idx) {
                return fileRepository.findFileByCategoryAndIdx(category, idx);
        }
}
