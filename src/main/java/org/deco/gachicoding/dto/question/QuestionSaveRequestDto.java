package org.deco.gachicoding.dto.question;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.question.Question;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class QuestionSaveRequestDto {

    @ApiModelProperty(value = "작성자 번호", required = true, example = "1")
    @NotNull
    private Long userIdx;

    @ApiModelProperty(value = "질문 제목", required = true, example = "Spring Security 가르쳐 주실 분")
    @NotNull
    private String queTitle;

    @ApiModelProperty(value = "질문 내용", required = true, example = "Spring Security 개어렵네염")
    @NotNull
    private String queContent;

    @ApiModelProperty(value = "질문 관련 에러메시지", required = false, example = "에러코드는 이래용")
    @Nullable
    private String queError;

    @ApiModelProperty(value = "질문 카테고리", notes = "이거 지금 잘 모르겠음", required = false, example = "Spring Security 개어렵네염")
    @Nullable
    private String queCategory;

    @Builder
    public QuestionSaveRequestDto(Long userIdx, String queTitle, String queContent, String queError, String queCategory) {
        this.userIdx = userIdx;
        this.queTitle = queTitle;
        this.queContent = queContent;
        this.queError = queError;
        this.queCategory = queCategory;
    }

    public Question toEntity() {
        return Question.builder()
                .queTitle(queTitle)
                .queContent(queContent)
                .queError(queError)
                .queCategory(queCategory)
                .build();
    }
}
