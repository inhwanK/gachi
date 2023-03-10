package org.deco.gachicoding.unit.post.question.application;

import org.deco.gachicoding.common.factory.post.question.QuestionMock;
import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.exception.post.question.QuestionAlreadyActiveException;
import org.deco.gachicoding.exception.post.question.QuestionAlreadyInactiveException;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.deco.gachicoding.post.question.application.dto.QuestionAssembler;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    QuestionService questionService;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    UserRepository userRepository;

    User questioner;

    @BeforeEach
    public void setUp() {
        questioner = UserMock.builder()
                .userEmail("1234@1234.com")
                .build();
    }


    @Test
    @DisplayName("질문을 등록한다.") // Assembler 테스트 아닌가?, 어쩌면 Mock을 쓰는 것의 단점 일수도 있겠다.
    public void register_Question_Success() {
        // given
        QuestionDto.SaveRequestDto dto = QuestionDto.SaveRequestDto.builder()
                .queTitle("새로운 질문")
                .queGeneralContent("새로운 질문의 일반적인 설명들")
                .queCodeContent("```java class Test{}```")
                .queErrorContent("error ~~~")
                .build();

        Question question = QuestionMock.builder()
                .questioner(questioner)
                .queTitle("새로운 질문")
                .queContents(
                        QuestionContents.builder()
                                .queGeneralContent("새로운 질문의 일반적인 설명들")
                                .queCodeContent("```java class Test{}```")
                                .queErrorContent("error ~~~")
                                .build()
                ).build();

        given(userRepository.findByUserEmail(eq("1234@1234.com")))
                .willReturn(Optional.of(questioner));
        given(questionRepository.save(any(Question.class)))
                .willReturn(question);

        // when
        questionService.registerQuestion(questioner.getUserEmail(), dto);
        Question expectedQuestion = QuestionAssembler.question(questioner, dto);

        // then
        assertThat(question)
                .usingRecursiveComparison()
                .ignoringFields("queIdx", "createdAt", "updatedAt")
                .isEqualTo(
                        expectedQuestion
                );
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

    @Test
    @DisplayName("질문을 비활성화한다.")
    public void disable_Question_Success() {

        // given
        Question question = QuestionMock.builder()
                .queIdx(1L)
                .questioner(questioner)
                .build();

        given(questionRepository.findById(eq(1L)))
                .willReturn(Optional.of(question));

        assertThat(question.getQueEnabled()).isTrue();
        // when
        questionService.disableQuestion(1L);

        // then
        assertThat(question.getQueEnabled()).isFalse();
    }


    @Test
    @DisplayName("이미 비활성화된 질문을 비활성화하려고하면 예외가 발생한다.")
    public void disable_Question_Exception() {

        // given
        Question question = QuestionMock.builder()
                .queIdx(1L)
                .questioner(questioner)
                .queEnabled(false)
                .build();

        given(questionRepository.findById(eq(1L)))
                .willReturn(Optional.of(question));

        assertThat(question.getQueEnabled()).isFalse();

        // then
        assertThatThrownBy(
                () -> questionService.disableQuestion(1L)
        )
                .isInstanceOf(QuestionAlreadyInactiveException.class)
                .hasMessageContaining("이미 비활성화 된 질문입니다.");
    }

    @Test
    @DisplayName("질문을 활성화한다.")
    public void enable_Question_Success() {

        // given
        Question question = QuestionMock.builder()
                .queIdx(1L)
                .questioner(questioner)
                .queEnabled(false)
                .build();

        given(questionRepository.findById(eq(1L)))
                .willReturn(Optional.of(question));

        assertThat(question.getQueEnabled()).isFalse();
        // when
        questionService.enableQuestion(1L);

        // then
        assertThat(question.getQueEnabled()).isTrue();
    }

    @Test
    @DisplayName("이미 활성화된 질문을 활성화하려고하면 예외가 발생한다.")
    public void enable_Question_Exception() {

        // given
        Question question = QuestionMock.builder()
                .queIdx(1L)
                .questioner(questioner)
                .build();

        given(questionRepository.findById(eq(1L)))
                .willReturn(Optional.of(question));

        assertThat(question.getQueEnabled()).isTrue();

        // then
        assertThatThrownBy(
                () -> questionService.enableQuestion(1L)
        )
                .isInstanceOf(QuestionAlreadyActiveException.class)
                .hasMessageContaining("이미 활성화 된 질문입니다.");
    }
}