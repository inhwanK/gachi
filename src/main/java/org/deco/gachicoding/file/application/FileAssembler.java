package org.deco.gachicoding.file.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.file.domain.File;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileAssembler {

    private FileAssembler() {}

    public static File file(Long articleIdx, String articleCategory, ObjectMetadata objectMetadata) {
        // 로그 위치 옮기기
//        log.info("articleIdx : " + articleIdx);
//        log.info("articleCategory : " + articleCategory);
//        log.info("temporaryFileName : " + temporaryFileName);
//        log.info("origFileName : " + getOriginFileName(temporaryFileName));
//        log.info("origFileExtension : " + getOriginFileExtension(temporaryFileName));
//        log.info("saveFileName : " + saveFileName);
//        log.info("filePath : " + String.format(s3Format, articleCategory, articleIdx, saveFileName));

        return File.builder()
                .articleIdx(articleIdx)
                .articleCategory(articleCategory)
                .objectMetadata(objectMetadata)
                .build();
    }
}
