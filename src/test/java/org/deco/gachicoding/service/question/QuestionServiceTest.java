package org.deco.gachicoding.service.question;

import org.deco.gachicoding.dto.question.QuestionResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuestionServiceTest {
    @Autowired
    QuestionService questionService;

    Long queIdx;

    String queTitle = "질문 테스트 제목 고양이 병아리(테스트)";
    String queContent = "질문 테스트 내용 강아지 병아리(테스트)";
    String queError = "질문 테스트 에러 소스";
    String queCategory = "자바";

    @BeforeEach
    void before() {
        QuestionSaveRequestDto dto = QuestionSaveRequestDto.builder()
                .userIdx(Long.valueOf(1))
                .queTitle(queTitle)
                .queContent(queContent)
                .queError(queError)
                .queCategory(queCategory)
                .build();

        queIdx = questionService.registerQuestion(dto);
    }

    @AfterEach
    void after() {
        if (queIdx != null) {
            questionService.removeQuestion(this.queIdx);
        }
        queIdx = null;
    }

    @Test
    void 질문_작성() {
        QuestionResponseDto responseDto = questionService.getQuestionDetailById(queIdx);

        assertEquals(queTitle, responseDto.getQueTitle());
        assertEquals(queContent, responseDto.getQueContent());
        assertEquals(queError, responseDto.getQueError());
        assertEquals(queCategory, responseDto.getQueCategory());
    }

    @Test
    public void 질문_리스트_조회() {
        String keyword = "병아리(테스트)";

        Page<QuestionResponseDto> questionList = questionService.getQuestionListByKeyword(keyword, 0);

        assertNotEquals(questionList.getTotalElements(), 1);
    }

    @Test
    public void 인덱스로_질문_수정() {
        String updateTitle = "질문 테스트 제목 고양이 병아리(수정 테스트)";
        String updateContent = "질문 테스트 내용 고양이 병아리(수정 테스트)";
        String updateError = "질문 테스트 에러 소스(수정)";
        String updateCategory = "파이썬";

        QuestionUpdateRequestDto updateQuestion = QuestionUpdateRequestDto.builder()
                .queTitle(updateTitle)
                .queContent(updateContent)
                .queError(updateError)
                .queCategory(updateCategory)
                .build();

        questionService.modifyQuestionById(queIdx, updateQuestion);

        QuestionResponseDto responseDto = questionService.getQuestionDetailById(queIdx);

        assertNotEquals(queTitle, responseDto.getQueTitle());
        assertEquals(updateTitle, responseDto.getQueTitle());

        assertNotEquals(queContent, responseDto.getQueContent());
        assertEquals(updateContent, responseDto.getQueContent());

        assertNotEquals(queError, responseDto.getQueError());
        assertEquals(updateError, responseDto.getQueError());

        assertNotEquals(queCategory, responseDto.getQueCategory());
        assertEquals(updateCategory, responseDto.getQueCategory());
    }

    @Test
    public void 인덱스로_질문_비활성화() {
        questionService.disableQuestion(queIdx);

        QuestionResponseDto responseDto = questionService.getQuestionDetailById(queIdx);

        assertEquals(responseDto.getQueActivated(), false);
    }

    @Test
    public void 인덱스로_공지사항_활성화() {
        questionService.enableQuestion(queIdx);

        QuestionResponseDto responseDto = questionService.getQuestionDetailById(queIdx);

        assertEquals(responseDto.getQueActivated(), true);
    }

    @Test
    public void 공지사항_삭제() {
        questionService.removeQuestion(queIdx);
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionDetailById(queIdx));
        queIdx = null;
    }
    
    // 테스트 순서 정하기
}
