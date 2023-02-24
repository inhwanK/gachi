package org.deco.gachicoding.post.question.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

@Getter
public class QuestionListResponseDto {

    private Long queIdx;
    private User questioner;
    private String queTitle;
    private QuestionContents queContents;
    private boolean queSolved;
    private boolean queLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionListResponseDto(
            Long queIdx,
            User questioner,
            String queTitle,
            QuestionContents queContents,
            boolean queSolved,
            boolean queLocked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.queIdx = queIdx;
        this.questioner = questioner;
        this.queTitle = queTitle;
        this.queContents = queContents;
        this.queSolved = queSolved;
        this.queLocked = queLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
