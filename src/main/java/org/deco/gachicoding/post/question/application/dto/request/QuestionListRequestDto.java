package org.deco.gachicoding.post.question.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class QuestionListRequestDto {

    private String keyword;

    private Pageable pageable;

    @Builder
    public QuestionListRequestDto(String keyword, Pageable pageable) {
        this.keyword = keyword;
        this.pageable = pageable;
    }
}
