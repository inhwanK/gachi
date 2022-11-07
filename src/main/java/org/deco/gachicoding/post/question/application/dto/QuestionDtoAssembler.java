package org.deco.gachicoding.post.question.application.dto;

import org.deco.gachicoding.post.question.application.dto.request.QuestionSaveRequestDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionDetailResponseDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;

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
                .map(QuestionListResponseDto::new)
                .collect(toList());
    }

    public static QuestionDetailResponseDto questionResponseDto(Question question) {
        return new QuestionDetailResponseDto(question);
    }

}
