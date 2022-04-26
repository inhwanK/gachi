package org.deco.gachicoding.dto.answer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.answer.Answer;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AnswerResponseDto {

    private Long asIdx;
    private Long userIdx;
    private Long qsIdx;
    private String asContent;
    private boolean asSelect;
    private boolean asActivated;
    private LocalDateTime asRegdate;

    @Builder
    public AnswerResponseDto(Answer answer) {
        this.asIdx = answer.getAsIdx();
        this.userIdx = answer.getUser().getUserIdx();
        this.qsIdx = answer.getQuestion().getQsIdx();
        this.asContent = answer.getAsContent();
        this.asSelect = answer.isAsSelect();
        this.asActivated = answer.isAsActivated();
        this.asRegdate = answer.getAsRegdate();
    }
}
