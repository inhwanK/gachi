package org.deco.gachicoding.post.question.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionBasicRequestDto {

    private String userEmail;

    private Long queIdx;

    @Builder
    public QuestionBasicRequestDto(String userEmail, Long queIdx) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
    }
}
