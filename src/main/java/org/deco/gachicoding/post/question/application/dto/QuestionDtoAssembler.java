package org.deco.gachicoding.post.question.application.dto;

import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.presentation.dto.AnswerAssembler;
import org.deco.gachicoding.post.answer.presentation.dto.response.AnswerResponse;
import org.deco.gachicoding.post.question.application.dto.request.QuestionSaveRequestDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionDetailResponseDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionDtoAssembler {

    private QuestionDtoAssembler() {}

    public static Question question(User user, QuestionSaveRequestDto dto) {
        return Question.builder()
                .questioner(user)
                .queTitle(dto.getQueTitle())
                .queContents(dto.getQueContents())
                .build();
    }

    public static List<QuestionListResponseDto> questionResponseDtos(List<Question> questions) {
        return questions.stream()
                .map(QuestionDtoAssembler::convertForm)
                .collect(toList());
    }

    private static QuestionListResponseDto convertForm(Question question) {
        return QuestionListResponseDto.builder()
                .queIdx(question.getQueIdx())
                .questioner(question.getQuestioner())
                .queTitle(question.getQueTitle())
                .queContents(question.getQueContents())
                .queSolved(question.getQueSolved())
                .queLocked(question.getQueEnabled())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    public static QuestionDetailResponseDto questionResponseDto(Question question) {
        return QuestionDetailResponseDto.builder()
                .queIdx(question.getQueIdx())
                .questioner(question.getQuestioner())
                .answers(answerResponses(question))
                .queTitle(question.getQueTitle())
                .queContents(question.getQueContents())
                .queSolved(question.getQueSolved())
                .queLocked(question.getQueEnabled())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    private static List<AnswerResponse> answerResponses(Question question) {
        List<Answer> answers = question.getAnswers();

        return answers.stream()
                .map(AnswerAssembler::answerResponse)
                .collect(toList());
    }

}
