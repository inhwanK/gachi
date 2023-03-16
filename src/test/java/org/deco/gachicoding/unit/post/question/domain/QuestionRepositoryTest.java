package org.deco.gachicoding.unit.post.question.domain;

import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.common.factory.post.question.QuestionMock;
import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Slf4j
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    List<Question> questions;
    User questioner;

    @BeforeEach
    void setUp() {
        questioner = UserMock.builder().build();
        userRepository.save(questioner);
        questions = createQuestions();
        questions.forEach(question -> questionRepository.save(question));

        testEntityManager.flush();
    }

    private List<Question> createQuestions() {
        Question question1 = QuestionMock.builder().questioner(questioner).queTitle("백엔드 질문").queContents("백엔드 질문인데여...", null, null).build();
        Question question2 = QuestionMock.builder().questioner(questioner).queTitle("프론트 질문").queContents("프론트 질문인데여...", null, null).build();
        Question question3 = QuestionMock.builder().questioner(questioner).queTitle("cs 질문").queContents("cs 질문인데여...", null, null).build();
        Question question4 = QuestionMock.builder().questioner(questioner).queTitle("알고리즘 질문").queContents("알고리즘 질문인데여...", null, null).build();
        return List.of(
                question1, question2, question3, question4
        );
    }

    @Test
    @DisplayName("질문을 등록한다.")
    public void save_Question_Success() {
        Question question = QuestionMock.builder()
                .queTitle("질문 등록할 때 질문")
                .build();

        Long idx = questionRepository.save(question).getQueIdx();
        questions = questionRepository.findAll();

        assertThat(questions).hasSize(5);
        assertThat(questionRepository.findById(idx).get())
                .isEqualTo(question);
    }

    @Test
    @DisplayName("질문을 조회한다.")
    public void find_Question_Success() {

        Question question = QuestionMock.builder()
                .build();

        Long idx = questionRepository.save(question).getQueIdx();

        Question actualQuestion = questionRepository.findById(idx).get();
        assertThat(question)
                .isEqualTo(actualQuestion);
    }

    @Test
    @DisplayName("검색어로 질문을 검색한다.")
    public void search_Question_Success() {

        long start = System.currentTimeMillis();
        log.info("시작 시간 : {}", start);
        questions = questionRepository.searchQuestionByGeneralContent("백엔드");
        long end = System.currentTimeMillis();
        log.info("종료 시간 : {}", end);
        log.info("duration : {}", (double) (end - start) / 1000);

        assertThat(questions).hasSize(1);
        assertThat(questions)
                .extracting("queContents")
                .extracting("queGeneralContent")
                .contains("백엔드 질문인데여...");
    }

    @Test
    @DisplayName("질문을 삭제한다.")
    public void delete_Question_Success() {
        questions = questionRepository.findAll();

        Question target = questions.get(0);
        Long idx = target.getQueIdx();
        questionRepository.deleteById(idx);

        questions = questionRepository.findAll();

        assertThat(questions).hasSize(3);
        assertThat(questions)
                .isNotIn(target);
    }
}
