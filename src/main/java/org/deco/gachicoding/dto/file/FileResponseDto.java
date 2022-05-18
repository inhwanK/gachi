package org.deco.gachicoding.dto.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.file.File;

@Getter
@Setter
@NoArgsConstructor
public class FileResponseDto {
    private String filePath;

    @Builder
    public FileResponseDto(File file) {
        this.filePath = file.getFilePath();
    }
}
