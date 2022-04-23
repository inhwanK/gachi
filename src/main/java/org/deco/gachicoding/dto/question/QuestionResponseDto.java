package org.deco.gachicoding.dto.question;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.question.Question;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QuestionResponseDto {

    private Long qIdx;
    private Long userIdx;
    private String qTitle;
    private String qContent;
    private String qError;
    private String qCategory;
    private boolean qSolve;
    private boolean qActivated;
    private LocalDateTime qRegdate;

    @Builder
    public QuestionResponseDto(Question question) {
        this.qIdx = question.getQIdx();
        this.userIdx = question.getUser().getUserIdx();
        this.qTitle = question.getQTitle();
        this.qContent = question.getQContent();
        this.qError = question.getQError();
        this.qCategory = question.getQCategory();
        this.qSolve = question.isQSolve();
        this.qActivated = question.isQActivated();
        this.qRegdate = question.getQRegdate();
    }
}
