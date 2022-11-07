package org.deco.gachicoding.post.question.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.post.answer.dto.response.AnswerResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionDetailResponse {
    private Long queIdx;

    private String userEmail;
    private String userNick;

    private List<AnswerResponseDto> answerList;

    private String queTitle;
    private String queContents;
    private Boolean queSolved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionDetailResponse(
            Long queIdx,
            String userEmail,
            String userNick,
            List<AnswerResponseDto> answerList,
            String queTitle,
            String queContents,
            Boolean queSolved,
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
