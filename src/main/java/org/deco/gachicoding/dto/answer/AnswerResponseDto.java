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

    private Long aIdx;
    private Long userIdx;
    private Long qIdx;
    private String aContent;
    private boolean aSelect;
    private boolean aActivated;
    private LocalDateTime aRegdate;

    @Builder
    public AnswerResponseDto(Answer answer) {
        this.aIdx = answer.getAIdx();
        this.userIdx = answer.getUser().getUserIdx();
        this.qIdx = answer.getQuestion().getQIdx();
        this.aContent = answer.getAContent();
        this.aSelect = answer.isASelect();
        this.aActivated = answer.isAActivated();
        this.aRegdate = answer.getARegdate();
    }
}
