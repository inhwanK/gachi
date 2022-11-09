package org.deco.gachicoding.post.question.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class QuestionUpdateRequest {

    @NotNull(message = "F0001")
    @Email(message = "F0002")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull(message = "F0001")
    @ApiModelProperty(value = "질문 번호", required = true, example = "1")
    private Long queIdx;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    @ApiModelProperty(value = "질문 제목", required = false, example = "수정된 제목")
    private String queTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
    @ApiModelProperty(value = "질문 내용", required = false, example = "수정된 내용")
    private String queContents;

    private QuestionUpdateRequest() {}

    @Builder
    public QuestionUpdateRequest(
            String userEmail,
            Long queIdx,
            String queTitle,
            String queContents
    ) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
        this.queTitle = queTitle;
        this.queContents = queContents;
    }
}
