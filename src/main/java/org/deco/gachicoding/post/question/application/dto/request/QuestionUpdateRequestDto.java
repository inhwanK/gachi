package org.deco.gachicoding.post.question.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionUpdateRequestDto {

    private String userEmail;

    private Long queIdx;

    private String queTitle;

    private String queContents;

    @Builder
    public QuestionUpdateRequestDto(
            String userEmail,
            Long queIdx,
            String queTitle,
            String queContents
    ) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
        this.queTitle = queTitle;
        this.queContents = queContents;
    }
}
