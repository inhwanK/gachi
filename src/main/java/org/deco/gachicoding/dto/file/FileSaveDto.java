package org.deco.gachicoding.dto.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.file.File;

@Getter
@Setter
@NoArgsConstructor
public class FileSaveDto {

    private Long articleIdx;
    private String articleCategory;
    private String origFilename;
    private String saveFilename;
    private String fileExt;
    private Long fileSize;
    private String filePath;

    @Builder
    public FileSaveDto(Long articleIdx, String articleCategory, String origFilename, String saveFilename, String fileExt, Long fileSize, String filePath) {
        this.articleIdx = articleIdx;
        this.articleCategory = articleCategory;
        this.origFilename = origFilename;
        this.saveFilename = saveFilename;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    public File toEntity() {
        return File.builder()
                .articleIdx(articleIdx)
                .articleCategory(articleCategory)
                .origFilename(origFilename)
                .saveFilename(saveFilename)
                .fileExt(fileExt)
                .fileSize(fileSize)
                .filePath(filePath)
                .build();
    }
}
