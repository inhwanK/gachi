package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.question.QuestionResponseDto;
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

    Page<QuestionResponseDto> getQuestionList(String keyword, Pageable pageable);

    QuestionResponseDto getQuestionDetail(Long queIdx);

    QuestionResponseDto modifyQuestion(QuestionUpdateRequestDto dto);

    ResponseEntity<ResponseState> disableQuestion(Long queIdx);

    ResponseEntity<ResponseState> enableQuestion(Long queIdx);

    ResponseEntity<ResponseState> removeQuestion(Long queIdx);
}
