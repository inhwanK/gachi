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
                .map(QuestionAssembler::questionListResponse)
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
                .queSolved(dto.isQueSolved())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    private static QuestionListResponse questionListResponse(QuestionListResponseDto dto) {
        return QuestionListResponse.builder()
                .queIdx(dto.getQueIdx())
                .userEmail(dto.getQuestioner().getUserEmail())
                .userNick(dto.getQuestioner().getUserNick())
                .queTitle(dto.getQueTitle())
                .queContents(dto.getQueContents())
                .queSolved(dto.isQueSolved())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
