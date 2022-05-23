package org.deco.gachicoding.dto;

import org.deco.gachicoding.dto.file.FileResponseDto;
import org.deco.gachicoding.dto.tag.TagResponseDto;

import java.util.List;

public interface ResponseDto {
    public abstract void setFiles(List<FileResponseDto> files);

    public abstract void setTags(List<TagResponseDto> tags);
}
