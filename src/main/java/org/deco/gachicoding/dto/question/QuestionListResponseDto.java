package org.deco.gachicoding.dto.question;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.answer.Answer;
import org.deco.gachicoding.domain.question.Question;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.dto.answer.AnswerResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionListResponseDto {

    private Long queIdx;
    private Long userIdx;
    private String queTitle;
    private String queContent;
    private String queError;
    private String queCategory;
    private Boolean queSolve;
    private Boolean queActivated;
    private LocalDateTime queRegdate;

    @Builder
    public QuestionListResponseDto(Question question) {
        setWriterInfo(question);
        this.queIdx = question.getQueIdx();
        this.queTitle = question.getQueTitle();
        this.queContent = question.getQueContent();
        this.queError = question.getQueError();
        this.queCategory = question.getQueCategory();
        this.queSolve = question.getQueSolve();
        this.queActivated = question.getQueActivated();
        this.queRegdate = question.getQueRegdate();
    }

    public void setWriterInfo(Question question) {
        User user = question.getUser();
        this.userIdx = user.getUserIdx();
    }
}
