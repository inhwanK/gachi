package org.deco.gachicoding.post.question.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionSaveRequest {

    private String queTitle;
    private String queContents;

    @Builder
    public QuestionSaveRequest(
            String queTitle,
            String queContents
    ) {
        this.queTitle = queTitle;
        this.queContents = queContents;
    }
}
