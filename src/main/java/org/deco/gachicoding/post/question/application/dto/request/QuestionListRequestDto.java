package org.deco.gachicoding.post.question.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

// 안쓰는 클래스인듯
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
