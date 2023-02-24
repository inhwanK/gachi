package org.deco.gachicoding.post.question.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;

@Getter
public class QuestionUpdateRequestDto {

    private String userEmail;
    private Long queIdx;
    private String queTitle;
    private QuestionContents queContents;

    @Builder
    public QuestionUpdateRequestDto(
            String userEmail,
            Long queIdx,
            String queTitle,
            QuestionContents queContents
    ) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
        this.queTitle = queTitle;
        this.queContents = queContents;
    }
}
