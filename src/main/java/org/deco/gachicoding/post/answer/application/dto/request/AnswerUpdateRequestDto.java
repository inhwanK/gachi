package org.deco.gachicoding.post.answer.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AnswerUpdateRequestDto {

    private String userEmail;

    private Long ansIdx;

    private String ansContents;

    @Builder
    public AnswerUpdateRequestDto(
            String userEmail,
            Long ansIdx,
            String ansContents
    ) {
        this.userEmail = userEmail;
        this.ansIdx = ansIdx;
        this.ansContents = ansContents;
    }
}
