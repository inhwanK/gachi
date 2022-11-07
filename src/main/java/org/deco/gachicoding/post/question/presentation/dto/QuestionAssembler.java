package org.deco.gachicoding.post.question.presentation.dto;

import org.deco.gachicoding.post.question.application.dto.response.QuestionDetailResponseDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionDetailResponse;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionListResponse;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class QuestionAssembler {

    private QuestionAssembler() {}

    public static List<QuestionListResponse> questionListResponse(List<QuestionListResponseDto> dtos) {
        return dtos.stream()
                .map(questionListResponse())
                .collect(toList());
    }

    public static QuestionDetailResponse questionDetailResponse(QuestionDetailResponseDto dto) {
        return QuestionDetailResponse.builder()
                .queIdx(dto.getQueIdx())
                .userEmail(dto.getQuestioner().getUserEmail())
                .userNick(dto.getQuestioner().getUserNick())
                .answerList(dto.getAnswerList())
                .queTitle(dto.getQueTitle())
                .queContents(dto.getQueContents())
                .queSolved(dto.getQueSolved())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    private static Function<QuestionListResponseDto, QuestionListResponse> questionListResponse() {
        return questionListResponseDto -> QuestionListResponse.builder()
                .queIdx(questionListResponseDto.getQueIdx())
                .userEmail(questionListResponseDto.getQuestioner().getUserEmail())
                .userNick(questionListResponseDto.getQuestioner().getUserNick())
                .queTitle(questionListResponseDto.getQueTitle())
                .queContents(questionListResponseDto.getQueContents())
                .queSolved(questionListResponseDto.getQueSolved())
                .createdAt(questionListResponseDto.getCreatedAt())
                .updatedAt(questionListResponseDto.getUpdatedAt())
                .build();
    }
}
