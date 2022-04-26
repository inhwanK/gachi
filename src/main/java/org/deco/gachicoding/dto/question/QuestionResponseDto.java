package org.deco.gachicoding.dto.question;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.question.Question;
import org.deco.gachicoding.domain.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QuestionResponseDto {

    private Long qsIdx;
    private Long userIdx;
    private String qsTitle;
    private String qsContent;
    private String qsError;
    private String qsCategory;
    private boolean qsSolve;
    private boolean qsActivated;
    private LocalDateTime qsRegdate;

    @Builder
    public QuestionResponseDto(Question question) {
        User user = question.getUser();
        this.userIdx = user.getUserIdx();

        this.qsIdx = question.getQsIdx();
        this.qsTitle = question.getQsTitle();
        this.qsContent = question.getQsContent();
        this.qsError = question.getQsError();
        this.qsCategory = question.getQsCategory();
        this.qsSolve = question.isQsSolve();
        this.qsActivated = question.isQsActivated();
        this.qsRegdate = question.getQsRegdate();
    }
}
