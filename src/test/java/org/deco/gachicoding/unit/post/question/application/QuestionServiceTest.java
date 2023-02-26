package org.deco.gachicoding.unit.post.question.application;

import org.deco.gachicoding.common.factory.post.question.QuestionMock;
import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.deco.gachicoding.post.question.application.dto.QuestionDto;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    QuestionService questionService;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    Question question;
    User questioner;

    @BeforeEach
    public void setUp() {
        questioner = UserMock.builder()
                .userEmail("1234@1234.com")
                .build();

        question = QuestionMock.builder().build();
    }


    @Test
    @DisplayName("질문을 등록한다.")
    public void register_Question_Success() {
        // given
        QuestionDto.SaveRequestDto dto = QuestionDto.SaveRequestDto.builder()
                .queTitle("새로운 질문")
                .queGeneralContent("새로운 질문의 일반적인 설명들")
                .queCodeContent("```java class Test{}```")
                .queErrorContent("error ~~~")
                .build();

        Question question = QuestionMock.builder()
                .queIdx(1L)
                .build();

        given(userRepository.findByUserEmail(eq("1234@1234.com")))
                .willReturn(Optional.of(questioner));
        given(questionRepository.save(any(Question.class)))
                .willReturn(question);

        assertThat(questionService.registerQuestion(questioner.getUserEmail(), dto))
                .isEqualTo(1L);
    }

    @Test
    @DisplayName("질문을 수정한다.")
    public void update_Question_Success() {

        // given
        QuestionDto.UpdateRequestDto dto = QuestionDto.UpdateRequestDto.builder()
                .queIdx(1L)
                .queTitle("수정된 질문")
                .userEmail("1234@1234.com")
                .queGeneralContent("수정된 질문의 일반적인 설명들")
                .queCodeContent("```java class Update{}```")
                .queErrorContent("update error ~~~")
                .build();

        Question question = QuestionMock.builder()
                .queIdx(1L)
                .questioner(questioner)
                .build();


        given(questionRepository.findById(eq(dto.getQueIdx())))
                .willReturn(Optional.of(question));


        // when
        questionService.modifyQuestion(dto);

        // then
        assertThat(question.getQueIdx()).isEqualTo(1L);
        assertThat(question.getQueTitle()).isEqualTo("수정된 질문");
        assertThat(question.getQueContents())
                .usingRecursiveComparison()
                .isEqualTo(new QuestionContents(
                        "수정된 질문의 일반적인 설명들",
                        "```java class Update{}```",
                        "update error ~~~"
                ));
    }
}
