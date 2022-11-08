package org.deco.gachicoding.post.question.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.dto.response.AnswerResponseDto;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class QuestionDetailResponseDto {

    private Long queIdx;
    private User questioner;
    private List<AnswerResponseDto> answerList = new ArrayList<>();
    private String queTitle;
    private String queContents;
    private boolean queSolved;
    private boolean queLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionDetailResponseDto(
            Long queIdx,
            User questioner,
            List<Answer> answers,
            String queTitle,
            String queContents,
            boolean queSolved,
            boolean queLocked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.queIdx = queIdx;
        this.questioner = questioner;

        setAnswerList(answers);

        this.queTitle = queTitle;
        this.queContents = queContents;
        this.queSolved = queSolved;
        this.queLocked = queLocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setAnswerList(List<Answer> answers) {
        for(Answer ans : answers) {
            System.out.println("ansId = "+ans.getAnsIdx());
            System.out.println("ansContents = "+ans.getAnsContents());

            answerList.add(
                    AnswerResponseDto.builder()
                    .answer(ans).build()
            );
        }
    }

//    @Override
//    public void setFiles(List<FileResponseDto> files) {
//        this.files = files;
//    }
//
//    @Override
//    public void setTags(List<TagResponseDto> tags) {
//        this.tags = tags;
//    }
}
