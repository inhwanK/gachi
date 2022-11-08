package org.deco.gachicoding.post.answer.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.Question;

import java.time.LocalDateTime;

@Getter
public class AnswerResponseDto {
    private Long ansIdx;
    private String userEmail;
    private String userNick;
    private Long queIdx;
    private String ansContent;
    private boolean ansSelected;
    private boolean ansLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public AnswerResponseDto(Answer answer) {
//        setWriterInfo(answer);
        this.userEmail = answer.getAnswerer().getUserEmail();
        this.userNick = answer.getAnswerer().getUserNick();
        setQuestionInfo(answer);
        this.ansIdx = answer.getAnsIdx();
        this.ansContent = answer.getAnsContents();
        this.ansSelected = answer.getAnsSelected();
        this.ansLocked = answer.getAnsLocked();
        this.createdAt = answer.getCreatedAt();
        this.updatedAt = answer.getUpdatedAt();
    }

    private void setQuestionInfo(Answer answer) {
        Question question = answer.getQuestion();
        this.queIdx = question.getQueIdx();
    }

//    private void setWriterInfo(Answer answer) {
//        User user = answer.getWriter();
//        this.writerIdx = user.getUserIdx();
//    }

}
