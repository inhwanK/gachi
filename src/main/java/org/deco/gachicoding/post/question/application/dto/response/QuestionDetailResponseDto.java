package org.deco.gachicoding.post.question.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.presentation.dto.response.AnswerResponse;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class QuestionDetailResponseDto {

    private Long queIdx;
    private User questioner;
    private List<AnswerResponse> answers;
    private String queTitle;
    private QuestionContents queContents;
    private boolean queSolved;
    private boolean queLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionDetailResponseDto(
            Long queIdx,
            User questioner,
            List<AnswerResponse> answers,
            String queTitle,
            QuestionContents queContents,
            boolean queSolved,
            boolean queLocked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.queIdx = queIdx;
        this.questioner = questioner;

        this.answers = answers;

        this.queTitle = queTitle;
        this.queContents = queContents;
        this.queSolved = queSolved;
        this.queLocked = queLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
