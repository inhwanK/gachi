package org.deco.gachicoding.post.answer.presentation.dto;

import org.deco.gachicoding.post.answer.application.dto.request.AnswerSaveRequestDto;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerBasicRequestDto;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerUpdateRequestDto;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.presentation.dto.request.AnswerSaveRequest;
import org.deco.gachicoding.post.answer.presentation.dto.request.AnswerUpdateRequest;
import org.deco.gachicoding.post.answer.presentation.dto.response.AnswerResponse;
import org.deco.gachicoding.user.domain.User;

public class AnswerAssembler {

    private AnswerAssembler() {}

    public static AnswerSaveRequestDto answerSaveRequestDto(
            AnswerSaveRequest request
    ) {
        return AnswerSaveRequestDto.builder()
                .userEmail(request.getUserEmail())
                .queIdx(request.getQueIdx())
                .ansContents(request.getAnsContents())
                .build();
    }

    public static AnswerUpdateRequestDto answerUpdateRequestDto(
            AnswerUpdateRequest request
    ) {
        return AnswerUpdateRequestDto.builder()
                .userEmail(request.getUserEmail())
                .ansIdx(request.getAnsIdx())
                .ansContents(request.getAnsContents())
                .build();
    }

    public static AnswerBasicRequestDto answerBasicRequestDto(
            Long ansIdx,
            String userEmail
    ) {
        return AnswerBasicRequestDto.builder()
                .ansIdx(ansIdx)
                .userEmail(userEmail)
                .build();
    }

    public static AnswerResponse answerResponse(Answer answer) {

        User answerer = answer.getAnswerer();

        return AnswerResponse.builder()
                .ansIdx(answer.getAnsIdx())
                .userEmail(answerer.getUserEmail())
                .userNick(answerer.getUserNick())
                .ansContents(answer.getAnsContents())
                .ansSelected(answer.getAnsSelected())
                .ansLocked(answer.getAnsLocked())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }
}
