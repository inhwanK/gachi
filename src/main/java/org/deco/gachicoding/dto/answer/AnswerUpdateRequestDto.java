package org.deco.gachicoding.dto.answer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AnswerUpdateRequestDto {

    private String ansContent;

    @Builder
    public AnswerUpdateRequestDto(String ansContent) {
        this.ansContent = ansContent;
    }
}
