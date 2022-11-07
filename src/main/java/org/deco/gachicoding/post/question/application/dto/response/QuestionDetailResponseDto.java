package org.deco.gachicoding.post.question.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.answer.dto.response.AnswerResponseDto;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDetailResponseDto {

    private Long queIdx;
    private User questioner;
    private List<AnswerResponseDto> answerList = new ArrayList<>();
    private String queTitle;
    private String queContents;
    private Boolean queSolved;
    private Boolean queLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QuestionDetailResponseDto(Question question) {
        this.queIdx = question.getQueIdx();
        this.questioner = question.getQuestioner();

        setAnswerList(question);

        this.queTitle = question.getQueTitle();
        this.queContents = question.getQueContents();
        this.queSolved = question.getQueSolved();
        this.queLocked = question.getQueLocked();
        this.createdAt = question.getCreatedAt();
        this.updatedAt = question.getUpdatedAt();
    }

    public void setAnswerList(Question question) {
        for(Answer ans : question.getAnswers()) {
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
