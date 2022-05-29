package org.deco.gachicoding.dto.answer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class AnswerUpdateRequestDto {

    @NotNull
    @ApiModelProperty(value = "답변 번호", required = true, example = "1")
    private Long ansIdx;

    // 현재 상태에서는 필수긴 함 없으면 오류
    @NotNull
    @ApiModelProperty(value = "수정할 답변 내용", required = false, example = "수정된 답변 내용")
    private String ansContent;

    @Builder
    public AnswerUpdateRequestDto(Long ansIdx, String asContent) {
        this.ansIdx = ansIdx;
        this.ansContent = asContent;
    }
}
