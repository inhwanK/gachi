package org.deco.gachicoding.file.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FileResponseDto {

    private final String originFileName;
    private final String filePath;

    @Builder
    public FileResponseDto(
            String originFileName,
            String filePath
    ) {
        this.originFileName = originFileName;
        this.filePath = filePath;
    }
}
