package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerSelectRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AnswerService {

    Long registerAnswer(AnswerSaveRequestDto dto);

    Page<AnswerResponseDto> getAnswerList(String keyword, Pageable pageable);

    AnswerResponseDto getAnswerDetail(Long ansIdx);

    AnswerResponseDto modifyAnswer(AnswerUpdateRequestDto dto) throws RuntimeException;

    ResponseEntity<ResponseState> selectAnswer(AnswerSelectRequestDto dto);

    ResponseEntity<ResponseState> disableAnswer(Long ansIdx);

    ResponseEntity<ResponseState> enableAnswer(Long ansIdx);

    ResponseEntity<ResponseState> removeAnswer(Long ansIdx);
}
