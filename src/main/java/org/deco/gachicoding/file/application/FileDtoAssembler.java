package org.deco.gachicoding.file.application;

import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.file.domain.File;
import org.deco.gachicoding.file.infrastructure.FileNameGenerator;
import org.deco.gachicoding.post.board.application.dto.request.BoardSaveRequestDto;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class FileDtoAssembler {

    private static String s3Format;

    private static String s3Url;

    @Value("${cloud.aws.s3.url.format}")
    public void setS3Format(String s3Format) {
        this.s3Format = s3Format;
    }

    @Value("${cloud.aws.s3.url}")
    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    private FileDtoAssembler() {}

    public static File file(Long articleIdx, String articleCategory, String temporaryFileName) {
        String saveFileName = FileNameGenerator.md5(temporaryFileName) + getOriginFileExtension(temporaryFileName);

        log.info("articleIdx : " + articleIdx);
        log.info("articleCategory : " + articleCategory);
        log.info("temporaryFileName : " + temporaryFileName);
        log.info("origFileName : " + getOriginFileName(temporaryFileName));
        log.info("origFileExtension : " + getOriginFileExtension(temporaryFileName));
        log.info("saveFileName : " + saveFileName);
        log.info("filePath : " + String.format(s3Format, articleCategory, articleIdx, saveFileName));

        return File.builder()
                .articleIdx(articleIdx)
                .articleCategory(articleCategory)
                .originFilename(getOriginFileName(temporaryFileName))
                .saveFilename(saveFileName)
                .fileExt(getOriginFileExtension(temporaryFileName))
                .filePath(String.format(s3Format, articleCategory, articleIdx, saveFileName))
                .build();
    }

    private static String getOriginFileName(String temporaryFileName) {
        return temporaryFileName.substring(temporaryFileName.indexOf("_")).replaceFirst("_", "");
    }

    private static String getOriginFileExtension(String temporaryFileName) {
        return temporaryFileName.substring(temporaryFileName.lastIndexOf("."));
    }
}
