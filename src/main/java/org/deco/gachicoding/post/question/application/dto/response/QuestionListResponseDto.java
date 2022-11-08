package org.deco.gachicoding.post.question.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QuestionListResponseDto {

    private Long queIdx;
    private User questioner;
    private String queTitle;
    private String queContents;
    private boolean queSolved;
    private boolean queLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionListResponseDto(
            Long queIdx,
            User questioner,
            String queTitle,
            String queContents,
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

//    @Override
//    public void setTags(List<TagResponseDto> tags) {
//        this.tags = tags;
//    }

//    public void setWriterInfo(Question question) {
//        User writer = question.getWriter().getUserIdx();
//        this.userIdx = writer.getUserIdx();
//    }
}
