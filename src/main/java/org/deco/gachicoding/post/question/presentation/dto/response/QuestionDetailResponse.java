package org.deco.gachicoding.post.question.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.post.answer.presentation.dto.response.AnswerResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionDetailResponse {
    private Long queIdx;

    private String userEmail;
    private String userNick;

    private List<AnswerResponse> answerList;

    private String queTitle;
    private String queContents;
    private boolean queSolved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionDetailResponse(
            Long queIdx,
            String userEmail,
            String userNick,
            List<AnswerResponse> answerList,
            String queTitle,
            String queContents,
            boolean queSolved,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.queIdx = queIdx;

        this.userEmail = userEmail;
        this.userNick = userNick;

        this.answerList = answerList;

        this.queTitle = queTitle;
        this.queContents = queContents;
        this.queSolved = queSolved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
