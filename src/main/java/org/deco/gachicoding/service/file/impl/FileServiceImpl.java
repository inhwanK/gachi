package org.deco.gachicoding.service.file.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.file.FileRepository;
import org.deco.gachicoding.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 리팩토링1 @Autowired -> @RequiredArgsConstructor
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
        private final FileRepository fileRepository;
        private final S3ServiceImpl s3Service;

        private final static String tmpRoot = "/src/main/resources/tempImg/";
        // 절대 경로를 위한 absolutePath
        private final String absolutePath = new File("").getAbsolutePath() + "\\";

        @PostConstruct
        public void init() {
                Path tmpPath = Path.of(tmpRoot);
                try {
                        createPath(tmpPath);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void createPath(Path path) throws IOException {
                if (!Files.exists(path)) {
                        Files.createDirectories(path);
                }
        }

        // 리팩토링 - 이미지 이름이 중복되면 안올라감
        public URI copyTempImage(MultipartHttpServletRequest mpRequest) throws IOException {
                MultipartFile multipartFile = null;
                String origFileName = null;
                String origFileExtension = null;
                String saveFileName = null;

                multipartFile = mpRequest.getFile("file");

                origFileName = multipartFile.getOriginalFilename();
//                origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));
//                saveFileName = origFileName + origFileExtension;

                File file = new File(absolutePath + tmpRoot + origFileName);
                multipartFile.transferTo(file);         // 저장


                URI tempImageURI = file.toURI();
                return tempImageURI;
        }

        public String moveImg(String content)throws Exception{

                Pattern nonValidPattern = Pattern
                        .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
                Matcher matcher = nonValidPattern.matcher(content);
                String img = "";
                while (matcher.find()) {
                        img = matcher.group(1);
                        // 구현 절차
                        // img(이미지 경로가 될듯)가 실제 존재 하는 파일인가 검사?
                        // 존재한다면 s3업로드
                        // 업로드 후 리플레이스
                        // ==> 존나 어렵겠네 ㅅㅂ

//                        img = img.replace("/img", "");
//                        content = content.replace("/img", "/image");
//                        File file =new File("C:\\mp\\tempImg\\"+img);
//                        file.renameTo(new File(filePath+img));



//                s3Service.upload(multipartFile);

                }
                return content;
        }

        private UUID getRandomString() {
                return UUID.randomUUID();
        }

}
