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
    private String userEmail;

    @NotNull(message = "F0001")
    private Long queIdx;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    private String queTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
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
