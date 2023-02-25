package org.deco.gachicoding.post.question.presentation.dto;

import org.deco.gachicoding.post.question.application.dto.request.QuestionBasicRequestDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionSaveRequestDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionUpdateRequestDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionDetailResponseDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.presentation.dto.request.QuestionSaveRequest;
import org.deco.gachicoding.post.question.presentation.dto.request.QuestionUpdateRequest;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionDetailResponse;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionListResponse;
import org.deco.gachicoding.user.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionAssembler {

    private QuestionAssembler() {}

    public static List<QuestionListResponse> questionListResponse(List<QuestionListResponseDto> dtos) {
        return dtos.stream()
                .map(QuestionAssembler::questionListResponse)
                .collect(toList());
    }

    private static QuestionListResponse questionListResponse(QuestionListResponseDto dto) {
        return QuestionListResponse.builder()
                .queIdx(dto.getQueIdx())
                .userEmail(dto.getQuestioner().getUserEmail())
                .userNick(dto.getQuestioner().getUserNick())
                .queTitle(dto.getQueTitle())
                .queGeneralContent(dto.getQueContents().getQueGeneralContent())
                .queCodeContent(dto.getQueContents().getQueCodeContent())
                .queErrorContents(dto.getQueContents().getQueErrorContent())
                .queSolved(dto.isQueSolved())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static QuestionUpdateRequestDto questionUpdateRequestDto(
            QuestionUpdateRequest request
    ) {
        return QuestionUpdateRequestDto.builder()
                .userEmail(request.getUserEmail())
                .queIdx(request.getQueIdx())
                .queTitle(request.getQueTitle())
                .queContents(request.getQueContents())
                .build();
    }

    public static QuestionBasicRequestDto questionBasicRequestDto(Long queIdx, String userEmail) {
        return QuestionBasicRequestDto.builder()
                .queIdx(queIdx)
                .userEmail(userEmail)
                .build();
    }
}
