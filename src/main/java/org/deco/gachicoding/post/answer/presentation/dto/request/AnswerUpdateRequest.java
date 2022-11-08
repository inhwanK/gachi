package org.deco.gachicoding.post.answer.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AnswerUpdateRequest {

    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "답변 번호", required = true, example = "1")
    private Long ansIdx;

    // 현재 상태에서는 필수긴 함 없으면 오류
    @NotNull
    @ApiModelProperty(value = "수정할 답변 내용", required = false, example = "수정된 답변 내용")
    private String ansContents;

    @Builder
    public AnswerUpdateRequest(
            String userEmail,
            Long ansIdx,
            String ansContents
    ) {
        this.userEmail = userEmail;
        this.ansIdx = ansIdx;
        this.ansContents = ansContents;
    }
}
