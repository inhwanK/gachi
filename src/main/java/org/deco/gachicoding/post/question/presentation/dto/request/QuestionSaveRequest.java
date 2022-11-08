package org.deco.gachicoding.post.question.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionSaveRequest {

    private String userEmail;

    private String queTitle;

    private String queContents;

//    @Nullable
//    @ApiModelProperty(value = "태그 목록", required = false, example = "Java")
//    private List<String> tags;

    @Builder
    public QuestionSaveRequest(
            String userEmail,
            String queTitle,
            String queContents
    ) {
        this.userEmail = userEmail;
        this.queTitle = queTitle;
        this.queContents = queContents;
    }
}
