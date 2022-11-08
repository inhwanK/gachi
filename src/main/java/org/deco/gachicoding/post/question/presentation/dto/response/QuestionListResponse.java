package org.deco.gachicoding.post.question.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionListResponse {

    // 이러한 형태도 한번 생각해 보자
//    private List<QuestionDetailResponse> questionDetailResponses;
    
    private Long queIdx;

    private String userEmail;
    private String userNick;

    private String queTitle;
    private String queContents;
    private boolean queSolved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionListResponse(
            Long queIdx,
            String userEmail,
            String userNick,
            String queTitle,
            String queContents,
            boolean queSolved,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.queIdx = queIdx;

        this.userEmail = userEmail;
        this.userNick = userNick;

        this.queTitle = queTitle;
        this.queContents = queContents;
        this.queSolved = queSolved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
