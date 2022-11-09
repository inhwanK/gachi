package org.deco.gachicoding.post.answer.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class AnswerBasicRequestDto {

    @NotNull
    @ApiModelProperty(value = "답변 번호", required = true, example = "1")
    private Long ansIdx;

    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "사용자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @Builder
    public AnswerBasicRequestDto(
            Long ansIdx,
            String userEmail
    ) {
        this.userEmail = userEmail;
        this.ansIdx = ansIdx;
    }
}
