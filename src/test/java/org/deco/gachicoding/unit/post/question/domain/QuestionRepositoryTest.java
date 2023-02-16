package org.deco.gachicoding.unit.post.question.domain;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.deco.gachicoding.common.factory.post.question.QuestionMock;
import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    User questioner = UserMock.builder()
            .userIdx(1L)
            .build();

    @BeforeEach
    void setUp() {
        log.info("setUp start");
        log.info("질문자 저장");
        userRepository.save(questioner);

        log.info("setUp end");

    }

    @Test
    @DisplayName("질문을 등록한다.")
    public void save_Question_Success() {
        Question question = QuestionMock.builder()
                .queIdx(1L)
                .questioner(questioner)
                .build();

        Long saveIdx = questionRepository.save(question).getQueIdx();

        assertThat(saveIdx).isEqualTo(question.getQueIdx());
    }

    @Test
    @DisplayName("질문을 조회한다.")
    public void find_Question_Success() {
        Question question = QuestionMock.builder()
                .queIdx(1L)
                .questioner(questioner)
                .build();

        Long saveIdx = questionRepository.save(question).getQueIdx();
        Long actualIdx = questionRepository.findById(saveIdx).get().getQueIdx();

        assertThat(saveIdx)
                .isEqualTo(actualIdx);
    }
}
