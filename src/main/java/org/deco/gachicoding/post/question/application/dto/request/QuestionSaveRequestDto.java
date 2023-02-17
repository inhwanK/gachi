package org.deco.gachicoding.post.question.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionSaveRequestDto {

    private String userEmail;
    private String queTitle;
    private String queContents;

    @Builder
    public QuestionSaveRequestDto(
            String userEmail,
            String queTitle,
            String queContents
    ) {
        this.userEmail = userEmail;
        this.queTitle = queTitle;
        this.queContents = queContents;
    }
}
