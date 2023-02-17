package org.deco.gachicoding.unit.post.question.application;

import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.deco.gachicoding.post.question.application.dto.request.QuestionSaveRequestDto;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    QuestionService questionService;

    @Mock
    QuestionRepository questionRepository;

    User questioner;

    @BeforeEach
    public void setUp() {
        questioner = UserMock.builder()
                .userEmail("1234@1234.com")
                .build();


    }


    @Test
    @DisplayName("질문을 등록한다.")
    public void register_Question_Success() {
        QuestionSaveRequestDto dto = QuestionSaveRequestDto.builder()
                .userEmail(questioner.getUserEmail())
                .build();

        // given
        Question question = Question.builder()
                .questioner(questioner)
                .build();

//        given(questionRepository.save())
//                .willReturn(question);
        fail("미구현");
    }

}
