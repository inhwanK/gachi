package org.deco.gachicoding.post.answer.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AnswerSaveRequest {

    @ApiModelProperty(value = "사용자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    private String userEmail;

    @ApiModelProperty(value = "답변할 질문 글 번호", required = true, example = "1")
    @NotNull
    private Long queIdx;

    @ApiModelProperty(value = "답변 내용", required = true, example = "Spring Security 아시나요~~ 얼마나 사랑했는지~~ 그댈 보면 자꾸 눈물이 나서~")
    @NotNull
    private String ansContents;

    @Builder
    public AnswerSaveRequest(
            String userEmail,
            Long queIdx,
            String ansContents
    ) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
        this.ansContents = ansContents;
    }
}
