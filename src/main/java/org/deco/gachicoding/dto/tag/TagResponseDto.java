package org.deco.gachicoding.dto.tag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagResponseDto {
    private String tagKeyword;

    public TagResponseDto(String tagKeyword) {
        this.tagKeyword = tagKeyword;
    }
}
