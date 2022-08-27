package org.deco.gachicoding.unit.user.domain;

import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@DataJpaTest // JPA 에 관련된 의존성 제공, 자동으로 롤백
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    User user;

    @BeforeEach
    void setUp() {
        user = MockUser.builder()
                .userEmail("inhan1009@naver.com")
                .build();


        userRepository.save(user);

//        testEntityManager.flush();
//        testEntityManager.clear();
    }

    @DisplayName("유저 이메일로 유저를 조회한다.")
    @Test
    public void findByUserEmail_Success() {
        User retrievedUser = userRepository.findByUserEmail(user.getUserEmail()).get();

        assertThat(retrievedUser).isEqualTo(user);
    }

    @DisplayName("가입되지 않은 유저 이메일로 유저를 조회할 수 없다.")
    @Test
    public void findByUserEmail_InvalidUserEmail_Empty() {
        assertThat(userRepository.findByUserEmail("invalidUser")).isEmpty();
    }

    @DisplayName("유저를 저장한다.")
    @Test
    public void save_Success() {
        User saveUser = MockUser.builder()
                .userEmail("saveTestEmail@test.com")
                .userNick("saveTestNick")
                .build();

        Long saveIdx = userRepository.save(saveUser).getUserIdx();

        Optional<User> retrievedUser = userRepository.findById(saveIdx);

        assertThat(saveUser).isEqualTo(retrievedUser.get());
    }

    @DisplayName("중복된 이메일로 유저를 저장할 수 없다.")
    @Test
    public void save_DuplicateEmail_Failed() {

        User duplicateEmailUser = MockUser.builder()
                .userEmail("inhan1009@naver.com")
                .userNick("saveTestNick")
                .build();

        assertThatThrownBy(
                () -> {
                    userRepository.save(duplicateEmailUser);
                }
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("유저번호로 유저를 삭제한다.")
    @Test
    public void deleteById_Success() {

        Optional<User> delUser = userRepository.findByUserEmail("inhan1009@naver.com");

        userRepository.deleteById(delUser.get().getUserIdx());

        assertThatThrownBy(
                () -> userRepository.findById(delUser.get().getUserIdx())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
