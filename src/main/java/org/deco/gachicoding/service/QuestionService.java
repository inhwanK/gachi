package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.question.QuestionDetailResponseDto;
import org.deco.gachicoding.dto.question.QuestionListResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {

    Long registerQuestion(QuestionSaveRequestDto dto);

    Page<QuestionListResponseDto> getQuestionList(String keyword, Pageable pageable);

    QuestionDetailResponseDto getQuestionDetail(Long queIdx);

    QuestionDetailResponseDto modifyQuestion(QuestionUpdateRequestDto dto);

    ResponseEntity<ResponseState> disableQuestion(Long queIdx);

    ResponseEntity<ResponseState> enableQuestion(Long queIdx);

    ResponseEntity<ResponseState> removeQuestion(Long queIdx);
}
