package org.deco.gachicoding.dto.answer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AnswerUpdateRequestDto {
    private Long ansIdx;
    private String ansContent;

    @Builder
    public AnswerUpdateRequestDto(Long ansIdx, String asContent) {
        this.ansIdx = ansIdx;
        this.ansContent = asContent;
    }
}
