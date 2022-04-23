package org.deco.gachicoding.service.question;

import org.deco.gachicoding.dto.question.QuestionResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
    QuestionResponseDto getQuestionDetailById(Long questionIdx);

    Page<QuestionResponseDto> getQuestionListByKeyword(String keyword, int page);

    Long registerQuestion(QuestionSaveRequestDto dto);

    QuestionResponseDto modifyQuestionById(Long questionIdx, QuestionUpdateRequestDto dto);

    Long removeQuestion(Long questionIdx);
}
