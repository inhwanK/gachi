package org.deco.gachicoding.unit.user.domain;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
                .userIdx(1L)
                .userEmail("inhan1009@naver.com")
                .build();

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {

        userRepository.deleteAll();
//        testEntityManager.getEntityManager()
//                .createNativeQuery("ALTER TABLE user RESTART IDENTITY")
//                .executeUpdate();
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
                () -> userRepository.save(duplicateEmailUser)
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("유저번호로 유저를 삭제한다.")
    @Test
    public void deleteById_Success() {
        userRepository.deleteById(1L);

        assertThatThrownBy(
                () -> userRepository.findById(1L)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
