package org.deco.gachicoding.post.answer.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AnswerResponse {
    private Long ansIdx;

    private String userEmail;
    private String userNick;

    private String ansContents;
    private boolean ansSelected;
    private boolean ansLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public AnswerResponse(
            Long ansIdx,
            String userEmail,
            String userNick,
            String ansContents,
            boolean ansSelected,
            boolean ansLocked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.ansIdx = ansIdx;

        this.userEmail = userEmail;
        this.userNick = userNick;

        this.ansContents = ansContents;
        this.ansSelected = ansSelected;
        this.ansLocked = ansLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
