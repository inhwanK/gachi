package org.deco.gachicoding.post.question.application.dto;

import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.presentation.dto.AnswerAssembler;
import org.deco.gachicoding.post.answer.presentation.dto.response.AnswerResponse;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionAssembler {

    private QuestionAssembler() {}

    public static Question question(
            User user,
            QuestionDto.SaveRequestDto dto
    ) {
        return Question.builder()
                .questioner(user)
                .queTitle(dto.getQueTitle())
                .queContents(dto.getQueContents())
                .queSolved(false)
                .queEnabled(true)
                .build();
    }

    public static List<QuestionDto.ListResponseDto> questionResponseDtos(List<Question> questions) {
        return questions.stream()
                .map(QuestionAssembler::convertForm)
                .collect(toList());
    }

    private static QuestionDto.ListResponseDto convertForm(Question question) {
        return QuestionDto.ListResponseDto.builder()
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

    public static QuestionDto.DetailResponseDto questionResponseDto(Question question) {
        return QuestionDto.DetailResponseDto.builder()
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

    public static QuestionContents questionContents(QuestionDto.UpdateRequestDto dto) {
        return QuestionContents.builder()
                .queGeneralContent(dto.getQueGeneralContent())
                .queCodeContent(dto.getQueCodeContent())
                .queErrorContent(dto.getQueErrorContent())
                .build();
    }
}
