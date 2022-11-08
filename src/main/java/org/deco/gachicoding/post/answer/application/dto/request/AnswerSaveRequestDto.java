package org.deco.gachicoding.post.answer.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class AnswerSaveRequestDto {

    private String userEmail;

    private Long queIdx;

    private String ansContents;

    @Builder
    public AnswerSaveRequestDto(String userEmail, Long queIdx, String ansContents) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
        this.ansContents = ansContents;
    }

    public Answer toEntity(User writer, Question question){
        return Answer.builder()
                .answerer(writer)
                .question(question)
                .ansContents(ansContents)
                .build();
    }
}
