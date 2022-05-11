package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AnswerService {

    Long registerAnswer(AnswerSaveRequestDto dto);

    Page<AnswerResponseDto> getAnswerList(String keyword, Pageable pageable);

    AnswerResponseDto getAnswerDetail(Long ansIdx);

    AnswerResponseDto modifyAnswer(AnswerUpdateRequestDto dto);

    void disableAnswer(Long ansIdx);

    void enableAnswer(Long ansIdx);

    Long removeAnswer(Long ansIdx);
}
