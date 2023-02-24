package org.deco.gachicoding.post.question.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionListResponse {
    
    private Long queIdx;

    private String userEmail;
    private String userNick;

    private String queTitle;
    private String queGeneralContent;
    private String queCodeContent;
    private String queErrorContents;
    private boolean queSolved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionListResponse(
            Long queIdx,
            String userEmail,
            String userNick,
            String queTitle,
            String queGeneralContent,
            String queCodeContent,
            String queErrorContents,
            boolean queSolved,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.queIdx = queIdx;

        this.userEmail = userEmail;
        this.userNick = userNick;

        this.queTitle = queTitle;
        this.queGeneralContent = queGeneralContent;
        this.queCodeContent = queCodeContent;
        this.queErrorContents = queErrorContents;
        this.queSolved = queSolved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
