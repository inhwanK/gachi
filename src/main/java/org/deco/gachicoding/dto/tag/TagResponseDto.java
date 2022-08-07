package org.deco.gachicoding.dto.tag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagResponseDto {
    private String tag;

    public TagResponseDto(String tag) {
        this.tag = tag;
    }
}
