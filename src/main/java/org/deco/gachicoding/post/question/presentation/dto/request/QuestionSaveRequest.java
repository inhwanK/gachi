package org.deco.gachicoding.post.question.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;

@Getter
@NoArgsConstructor
public class QuestionSaveRequest {

    private String queTitle;
    private QuestionContents queContents;

    @Builder
    public QuestionSaveRequest(
            String queTitle,
            String queGeneralContent,
            String queCodeContent,
            String queErrorContent
    ) {
        this.queTitle = queTitle;
        this.queContents = QuestionContents.builder()
                .queGeneralContent(queGeneralContent)
                .queCodeContent(queCodeContent)
                .queErrorContent(queErrorContent)
                .build();
    }
}
