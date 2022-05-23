package org.deco.gachicoding.dto.file;

import lombok.Getter;
import lombok.Setter;
import org.deco.gachicoding.domain.file.File;

@Getter
@Setter
public class FileResponseDto {
    private String filePath;

    public FileResponseDto(File file) {
        this.filePath = file.getFilePath();
    }
}
