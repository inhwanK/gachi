package org.deco.gachicoding.service.question;

import org.deco.gachicoding.dto.question.QuestionResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {

    Long registerQuestion(QuestionSaveRequestDto dto);

    Page<QuestionResponseDto> getQuestionList(String keyword, Pageable pageable);

    QuestionResponseDto getQuestionDetail(Long queIdx);

    QuestionResponseDto modifyQuestion(QuestionUpdateRequestDto dto);

    void disableQuestion(Long queIdx);

    void enableQuestion(Long queIdx);

    Long removeQuestion(Long queIdx);
}
