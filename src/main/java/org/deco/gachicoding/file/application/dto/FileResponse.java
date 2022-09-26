package org.deco.gachicoding.file.application.dto;

import org.deco.gachicoding.file.application.dto.response.FileResponseDto;

import java.util.List;

public interface FileResponse {
    void setFiles(List<FileResponseDto> files);
}
