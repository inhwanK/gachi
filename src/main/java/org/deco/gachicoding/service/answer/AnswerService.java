package org.deco.gachicoding.service.answer;

import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface AnswerService {
    AnswerResponseDto getAnswerDetailById(Long answerIdx);

    Page<AnswerResponseDto> getAnswerListByKeyword(String keyword, int page);

    Long registerAnswer(AnswerSaveRequestDto dto);

    AnswerResponseDto modifyAnswerById(Long answerIdx, AnswerUpdateRequestDto dto);

    Long removeAnswer(Long answerIdx);
}
