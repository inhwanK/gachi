package org.deco.gachicoding.file.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FileResponseDto {

    private final String saveFileName;
    private final String filePath;

    @Builder
    public FileResponseDto(
            String saveFileName,
            String filePath
    ) {
        this.saveFileName = saveFileName;
        this.filePath = filePath;
    }
}
