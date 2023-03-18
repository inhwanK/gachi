package org.deco.gachicoding.post.question.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.post.answer.presentation.dto.response.AnswerResponse;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class QuestionDto {

    @Getter
    @NoArgsConstructor // 필요한 이유 확실하게 정리
    public static class SaveRequestDto {

        private String queTitle;
        private String queGeneralContent;
        private String queCodeContent;
        private String queErrorContent;

        @Builder
        public SaveRequestDto(
                String queTitle,
                String queGeneralContent,
                String queCodeContent,
                String queErrorContent
        ) {
            this.queTitle = queTitle;
            this.queGeneralContent = queGeneralContent;
            this.queCodeContent = queCodeContent;
            this.queErrorContent = queErrorContent;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequestDto {

        @NotNull(message = "F0001")
        private Long queIdx;
        private String userEmail;

        @NotNull(message = "F0001")
        @Size(max = 100, message = "F0004")
        private String queTitle;
        private String queGeneralContent;
        private String queCodeContent;
        private String queErrorContent;

        @Builder
        public UpdateRequestDto(
                Long queIdx,
                String userEmail,
                String queTitle,
                String queGeneralContent,
                String queCodeContent,
                String queErrorContent
        ) {
            this.queIdx = queIdx;
            this.userEmail = userEmail;
            this.queTitle = queTitle;
            this.queGeneralContent = queGeneralContent;
            this.queCodeContent = queCodeContent;
            this.queErrorContent = queErrorContent;
        }
    }


    @Getter
    public static class DetailResponseDto {

        private Long queIdx;
        private String userEmail;
        private String userNick;
        private List<AnswerResponse> answers;
        private String queTitle;
        private String queGeneralContent;
        private String queCodeContent;
        private String queErrorContent;
        private boolean queSolved;
        private boolean queLocked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        @Builder
        public DetailResponseDto(
                Long queIdx,
                User questioner,
                List<AnswerResponse> answers,
                String queTitle,
                QuestionContents queContents,
                Boolean queSolved,
                Boolean queLocked,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
        ) {
            this.queIdx = queIdx;
            this.userEmail = questioner.getUserEmail();
            this.userNick = questioner.getUserNick();
            this.answers = answers;
            this.queTitle = queTitle;
            this.queGeneralContent = queContents.getQueGeneralContent();
            this.queCodeContent = queContents.getQueCodeContent();
            this.queErrorContent = queContents.getQueErrorContent();
            this.queSolved = queSolved;
            this.queLocked = queLocked;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    @Getter
    public static class ListResponseDto {

        private Long queIdx;
        private User questioner;
        private String queTitle;
        private QuestionContents queContents;
        private boolean queSolved;
        private boolean queEnabled;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // entity 그대로 넣기
        public ListResponseDto(Question question) {
            this.queIdx = question.getQueIdx();
            this.questioner = question.getQuestioner(); // 조회가 또 일어나나?
            this.queTitle = question.getQueTitle();
            this.queContents = question.getQueContents();
            this.queSolved = question.getQueSolved();
            this.queEnabled = question.getQueEnabled();
            this.createdAt = question.getCreatedAt();
            this.updatedAt = question.getUpdatedAt();
        }

        @Builder
        public ListResponseDto(
                Long queIdx,
                User questioner,
                String queTitle,
                QuestionContents queContents,
                boolean queSolved,
                boolean queEnabled,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
        ) {
            this.queIdx = queIdx;
            this.questioner = questioner;
            this.queTitle = queTitle;
            this.queContents = queContents;
            this.queSolved = queSolved;
            this.queEnabled = queEnabled;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }
}
