package org.deco.gachicoding.unit.user.domain;

import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
                .userEmail("inhan1009@naver.com")
                .build();

        userRepository.save(user);
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

    @Disabled
    @DisplayName("유저를 저장한다.")
    @Test
    public void userSaveTest() {

        assertThat(false).isTrue();
    }

    @Disabled
    @DisplayName("유저를 삭제한다.")
    @Test
    public void deleteUserTest() {
        assertThat(false).isTrue();
    }

}
