package org.deco.gachicoding.post.question.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;

@Getter
@Builder
public class QuestionSaveRequestDto {

    private String userEmail;
    private String queTitle;
    private QuestionContents queContents;


    public QuestionSaveRequestDto(
            String userEmail,
            String queTitle,
            QuestionContents queContents
    ) {
        this.userEmail = userEmail;
        this.queTitle = queTitle;
        this.queContents = queContents;
    }
}
