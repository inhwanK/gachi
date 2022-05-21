package org.deco.gachicoding.dto.tag;

import lombok.Getter;
import lombok.Setter;
import org.deco.gachicoding.domain.tag.Tag;

@Getter
@Setter
public class TagSaveRequestDto {
    public String keyword;

    public Tag toEntity() {
        return Tag.builder()
                .keyword(this.keyword)
                .build();
    }
}
