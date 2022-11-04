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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

//        @Transactional
//        public FileResponse getFiles(Long idx, String category, FileResponse dto) {
//                List<FileResponseDto> result = new ArrayList<>();
//                List<File> fileList = fileRepository.findAllByArticleCategoryAndArticleIdx(category, idx);
//
//                for (File f : fileList) {
//                        result.add(new FileResponseDto(f));
//                }
//
//                dto.setFiles(result);
//                return dto;
//        }

        @Transactional
        public String extractImgSrc(Long idx, String content, String category) {
                // 정규 표현식 공부하자
                Pattern nonValidPattern = Pattern
                        .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");

                Matcher matcher = nonValidPattern.matcher(content);

                while (matcher.find()) {
                        String beforeImg = matcher.group(1);

                        // 구현 절차
                        // 추출된 이미지 링크 s3업로드
                        // File DB에 업로드
                        // content 리플레이스
                        String afterImg = uploadRealImg(idx, beforeImg, category);

                        log.info("beforeImg = " + beforeImg);
                        log.info("afterImg = " + afterImg);
                        content = content.replace(beforeImg, afterImg);
                        log.info(content);
                }

                return content;
        }

        private String uploadRealImg(Long idx, String path, String category) {

                log.info("path : " + path);

                String tamperingFilePath = path.replace(s3Url, "");

                log.info("tamperingFileName : " + tamperingFilePath);

                ObjectMetadata objectMetadata = s3Service.getObjectMetadata(tamperingFilePath);

                File file = FileAssembler.file(idx, category, objectMetadata);

                registerFile(file);

                String oldPath = tamperingFilePath;
                String newPath = file.getFilePath();

                log.info("oldPath : " + oldPath);
                log.info("newPath : " + newPath);

                return s3Service.replaceS3(oldPath, newPath);
        }

//        private String uploadRealImg(Long idx, String path, String category) throws IOException {
//                String origFileName = null;
//                String origFileExtension = null;
//                String tamperingFileName = null;
//                String filePath = null;
//                String oldPath = null;
//                String newPath = null;
//                Long fileSize = null;
//
//                // 저장 경로 바꿔야 함 (날짜도 추가)
//                log.info("path : " + path);
//
//                // oldPath -> substring, indexOf, lastIndexOf -> 뒤에서 부터 인덱스 찾기
////                try {
////                        oldPath = URLDecoder.decode(path.substring(path.indexOf("temp")),"UTF-8");
////                } catch (UnsupportedEncodingException e) {
////                        log.error("Encoding Error");
////                        throw e;
////                }
//
//                oldPath = path.replace(s3Url, "");
//                tamperingFileName = oldPath.split("temp/")[1];  //[4] -> /의 수에따라 바뀜
//
//                origFileName = tamperingFileName.substring(tamperingFileName.indexOf("_")).replaceFirst("_", "");
//                origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));
//
//                // 날짜 추가
//                newPath = category + "/" + idx + "/" + tamperingFileName;
//                fileSize = convertFile(path, tamperingFileName);
//
//                filePath = s3Service.replaceS3(oldPath, newPath);
//                log.info("oldPath : " + oldPath);
//                log.info("tamperingFileName : " + tamperingFileName);
//                log.info("origFileName : " + origFileName);
//                log.info("origFileExtension : " + origFileExtension);
//                log.info("newPath : " + newPath);
//                log.info("fileSize : " + fileSize);
//                log.info("filePath : " + filePath);
//
//                FileSaveRequestDto dto = FileSaveRequestDto.builder()
//                        .articleIdx(idx)
//                        .articleCategory(category)
//                        .originFilename(origFileName)
//                        .fileExt(origFileExtension)
//                        .saveFilename(tamperingFileName)
//                        .fileSize(fileSize)
//                        .filePath(filePath)
//                        .build();
//
//                registerFile(dto);
//
//                return filePath;
//        }

//        private Long convertFile(String filePath, String tamperingFileName) throws IOException {
//                try {
//                        Long fileSize;
//
//                        URL url = new URL(filePath);
//                        String canonicalPath = new java.io.File("").getCanonicalPath();
//                        Path savePath = Paths.get(canonicalPath + "/src/main/resources/tempImg/", tamperingFileName);
//                        InputStream is = url.openStream();
//
//                        copyLocalFile(is, savePath);
//                        fileSize = getFileSize(savePath);
//                        deleteLocalFile(savePath);
//
//                        log.info("Success Convert File");
//
//                        return fileSize;
//                } catch (IOException e) {
//                        log.error("Failed Convert File");
//                        throw e;
//                }
//        }
//
//        private void copyLocalFile(InputStream is, Path savePath) throws IOException {
//                try {
//                        Files.copy(is, savePath);
//                        is.close();
//                } catch (IOException e) {
//                        log.error("Failed Copy Local File");
//                        throw e;
//                }
//        }
//
//        private void deleteLocalFile(Path savePath) throws IOException {
//                try {
//                        Files.delete(savePath);
//                } catch (IOException e) {
//                        log.error("Failed Delete Local File");
//                        throw e;
//                }
//        }
//
//        private Long getFileSize(Path savePath) throws IOException {
//                try {
//                        log.info("start Get File Size");
//                        Long bytes = Files.size(savePath);
//                        Long kilobyte = bytes/1024;
//                        Long megabyte = kilobyte/1024;
//                        return bytes;
//                } catch (IOException e) {
//                        log.info("Filed Get File Size");
//                        throw e;
//                }
//        }

}
