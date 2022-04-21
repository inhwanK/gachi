package org.deco.gachicoding.service.file.impl;

import org.deco.gachicoding.domain.file.FileRepository;
import org.deco.gachicoding.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

// 리팩토링1 @Autowired -> @RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
        private FileRepository fileRepository;
        private S3ServiceImpl s3Service;

        private final static String tmpRoot = "/src/main/resources/tempImg/";
        // 절대 경로를 위한 absolutePath
        private final String absolutePath = new File("").getAbsolutePath() + "\\";

        @Autowired
        public FileServiceImpl(FileRepository fileRepository, S3ServiceImpl s3Service) {
                this.fileRepository = fileRepository;
                this.s3Service = s3Service;

                Path tmpPath = Path.of(tmpRoot);
                try {
                        init(tmpPath);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void init(Path path) throws IOException {
                if (!Files.exists(path)) {
                        Files.createDirectories(path);
                }
        }

        public String copyTempImage(MultipartHttpServletRequest mpRequest) throws IOException {
                MultipartFile multipartFile = null;
                String origFileName = null;
                String origFileExtension = null;
                String saveFileName = null;

                multipartFile = mpRequest.getFile("file");

                s3Service.upload(multipartFile);

                origFileName = multipartFile.getOriginalFilename();
                origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));
                saveFileName = String.valueOf(getRandomString()) + origFileExtension;

                File file = new File(absolutePath + tmpRoot + saveFileName);
                multipartFile.transferTo(file);

                String imgPath = "/img/" + saveFileName;

                return imgPath;
        }

        private UUID getRandomString() {
                return UUID.randomUUID();
        }

}
