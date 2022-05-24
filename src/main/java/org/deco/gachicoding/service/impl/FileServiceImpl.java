package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.domain.file.File;
import org.deco.gachicoding.domain.file.FileRepository;
import org.deco.gachicoding.dto.ResponseDto;
import org.deco.gachicoding.dto.file.FileResponseDto;
import org.deco.gachicoding.dto.file.FileSaveDto;
import org.deco.gachicoding.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 리팩토링1 @Autowired -> @RequiredArgsConstructor
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
        private final FileRepository fileRepository;

//        private final static String tempRoot = "/src/main/resources/tempImg/";
//        // 절대 경로를 위한 absolutePath
//        private final static String absolutePath = new File("").getAbsolutePath() + "\\";
//        private final static Path path = Path.of(absolutePath + tempRoot);

//        @PostConstruct
//        public void init() {
//                try {
//                        if (!Files.exists(path)) {
//                                Files.createDirectories(path);
//                        }
//                } catch (IOException e) {
//                        e.printStackTrace();
//                }
//        }

        @Transactional
        public void registerFile(FileSaveDto fileSaveDto) {
                try {
                        log.info("tried Save File");
                        fileRepository.save(fileSaveDto.toEntity());
                        log.info("Success Save File");
                        // 구체적인 익셉션?
                } catch (Exception e) {
                        log.info("Error Save File");
                        e.printStackTrace();
                }
        }

        @Transactional
        public ResponseDto getFiles(Long boardIdx, String boardCategory, ResponseDto dto) {
                List<FileResponseDto> result = new ArrayList<>();
                List<File> fileList = fileRepository.findAllByBoardCategoryAndBoardIdx(boardCategory, boardIdx);

                for (File f : fileList) {
                        result.add(new FileResponseDto(f));
                }

                dto.setFiles(result);
                return dto;
        }

//        // 리팩토링 - 이미지 이름이 중복되면 안올라감
//        public String copyTempImage(MultipartHttpServletRequest mpRequest) throws IOException {
//                MultipartFile multipartFile = null;
//                String origFileName = null;
//                String origFileExtension = null;
//                String saveFileName = null;
//
//                multipartFile = mpRequest.getFile("file");
//
////                origFileName = multipartFile.getOriginalFilename();
////                origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));
////                saveFileName = origFileName + origFileExtension;
//
////                File file = new File(absolutePath + tempRoot + origFileName);
////                multipartFile.transferTo(file);         // 저장
//
////                return file.getAbsolutePath();
//
////                return s3Service.upload(multipartFile);
//                return null;
//        }

//        public String moveImg(String content) {
//                // 정규 표현식 공부하자
//                Pattern nonValidPattern = Pattern
//                        .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
//                Matcher matcher = nonValidPattern.matcher(content);
//                String img = "";
//                while (matcher.find()) {
//                        img = matcher.group(1);
//                        System.out.println("img : " + img);
//                        // 구현 절차
//                        // img(이미지 경로가 될듯)가 실제 존재 하는 파일인가 검사?
//                        // 존재한다면 s3업로드
//                        // 업로드 후 리플레이스
//                        // ==> 존나 어렵겠네 ㅅㅂ
//
////                        img = img.replace("/img", "");
////                        content = content.replace("/img", "/image");
////                        File file =new File("C:\\mp\\tempImg\\"+img);
////                        file.renameTo(new File(filePath+img));
//
//
//
////                s3Service.upload(multipartFile);
//
//                }
//                return content;
//        }

}
