package org.deco.gachicoding.dto.answer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AnswerUpdateRequestDto {

    private String ansContent;

    public AnswerUpdateRequestDto(String asContent) {
        this.ansContent = asContent;
    }
}
