package org.deco.gachicoding.dto.answer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AnswerUpdateRequestDto {

    private String aContent;

    public AnswerUpdateRequestDto(String aContent) {
        this.aContent = aContent;
    }
}
