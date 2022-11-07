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
    private String queContent;
    private Boolean queSolved;
    private Boolean queLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionListResponseDto(Question question) {
        this.questioner = question.getQuestioner();
        this.queIdx = question.getQueIdx();
        this.queTitle = question.getQueTitle();
        this.queContent = question.getQueContents();
        this.queSolved = question.getQueSolved();
        this.queLocked = question.getQueLocked();
        this.createdAt = question.getCreatedAt();
        this.updatedAt = question.getUpdatedAt();
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
