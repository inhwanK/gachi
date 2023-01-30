package org.deco.gachicoding.file.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.file.domain.ArticleType;
import org.deco.gachicoding.file.domain.File;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileAssembler {

    private FileAssembler() {}

    public static File file(
            Long articleIdx,
            ArticleType articleType,
            ObjectMetadata objectMetadata
    ) {
        // 로그 위치 옮기기
//        log.info("articleIdx : " + articleIdx);
//        log.info("articleType : " + articleType);
//        log.info("temporaryFileName : " + temporaryFileName);
//        log.info("origFileName : " + getOriginFileName(temporaryFileName));
//        log.info("origFileExtension : " + getOriginFileExtension(temporaryFileName));
//        log.info("saveFileName : " + saveFileName);
//        log.info("filePath : " + String.format(s3Format, articleType, articleIdx, saveFileName));

        return File.builder()
                .articleIdx(articleIdx)
                .articleType(articleType)
                .objectMetadata(objectMetadata)
                .build();
    }
}
