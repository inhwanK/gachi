package org.deco.gachicoding.post.answer.presentation.dto;

import org.deco.gachicoding.post.answer.application.dto.request.AnswerSaveRequestDto;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerUpdateRequestDto;
import org.deco.gachicoding.post.answer.presentation.dto.request.AnswerSaveRequest;
import org.deco.gachicoding.post.answer.presentation.dto.request.AnswerUpdateRequest;

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
}
