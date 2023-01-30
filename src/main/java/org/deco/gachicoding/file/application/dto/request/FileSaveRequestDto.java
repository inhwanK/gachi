package org.deco.gachicoding.file.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.file.domain.ArticleType;
import org.deco.gachicoding.file.domain.File;

@Getter
@Setter
@NoArgsConstructor
public class FileSaveRequestDto {

    private Long articleIdx;
    private ArticleType articleType;
    private String originFilename;
    private String saveFilename;
    private String fileExt;
    private Long fileSize;
    private String filePath;

    @Builder
    public FileSaveRequestDto(Long articleIdx, ArticleType articleType, String originFilename, String saveFilename, String fileExt, Long fileSize, String filePath) {
        this.articleIdx = articleIdx;
        this.articleType = articleType;
        this.originFilename = originFilename;
        this.saveFilename = saveFilename;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    public File toEntity() {
        return File.builder()
                .articleIdx(articleIdx)
                .articleType(articleType)
//                .originFilename(originFilename)
//                .saveFilename(saveFilename)
//                .fileExt(fileExt)
//                .filePath(filePath)
                .build();
    }
}
