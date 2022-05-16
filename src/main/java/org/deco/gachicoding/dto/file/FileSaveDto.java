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

    private Long boardIdx;
    private String boardCategory;
    private String origFilename;
    private String saveFilename;
    private String fileExt;
    private Long fileSize;
    private String filePath;

    @Builder
    public FileSaveDto(Long boardIdx, String boardCategory, String origFilename, String saveFilename, String fileExt, Long fileSize, String filePath) {
        this.boardIdx = boardIdx;
        this.boardCategory = boardCategory;
        this.origFilename = origFilename;
        this.saveFilename = saveFilename;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    public File toEntity() {
        return File.builder()
                .boardIdx(boardIdx)
                .boardCategory(boardCategory)
                .origFilename(origFilename)
                .saveFilename(saveFilename)
                .fileExt(fileExt)
                .fileSize(fileSize)
                .filePath(filePath)
                .build();
    }
}
