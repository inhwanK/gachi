package org.deco.gachicoding.unit.user.application;

import org.deco.gachicoding.common.factory.user.MockUser;
import org.deco.gachicoding.exception.user.password.InvalidPasswordUpdateException;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.PasswordUpdateRequestDto;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({MockitoExtension.class})
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();
    }

    @DisplayName("사용자를 생성한다.")
    @Test
    void createUser_Success() {

        // given
        UserSaveRequestDto dto = UserSaveRequestDto.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        given(userRepository.existsByUserEmail(any())).willReturn(false);
        given(userRepository.save(dto.toEntity())).willReturn(user);
        given(passwordEncoder.encode(any())).willReturn(any());

        // when
        Long userIdx = userService.createUser(dto);

        // then
        assertThat(userIdx).isEqualTo(user.getUserIdx());
    }

    @DisplayName("중복된 이메일을 가진 사용자는 회원가입을 할 수 없다.")
    @Test
    void createUser_Exception() {

        // given
        UserSaveRequestDto dto = UserSaveRequestDto.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        given(userRepository.existsByUserEmail(dto.getUserEmail()))
                .willReturn(true);

        // then
        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("중복된");
    }

    @DisplayName("유저가 입력한 비밀번호가 정확하게 확인된다.")
    @Test
    void confirmUser_Success_Return_True() {

        // given
//        given(userRepository.findByUserEmail("1234@1234.com")).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches(eq("1234"), anyString())).willReturn(true);

        // when
        boolean expected = userService.confirmUser("1234", "1234");

        // then
        assertThat(expected).isTrue();
    }

    @DisplayName("유저가 입력한 비밀번호가 정확하지 않다.")
    @Test
    void confirmUser_Success_Return_False() {

        // given
        given(passwordEncoder.matches(eq("12345"), anyString())).willReturn(false);

        // when
        boolean expected = userService.confirmUser("12345", "12345");

        // then
        assertThat(expected).isFalse();
    }

    @DisplayName("유저의 비밀번호를 변경한다.")
    @Test
    void updateUserPassword_Success() {

        // given
        PasswordUpdateRequestDto dto = new PasswordUpdateRequestDto(
                "newPassword",
                "newPassword"
        );

        given(userRepository.findByUserEmail("1234@1234.com")).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches("newPassword", "1234")).willReturn(false);
        given(passwordEncoder.encode("newPassword")).willReturn("changedPassword");

        // when
        userService.modifyUserPassword("1234@1234.com", dto);

        // then
        assertThat(user.getUserPassword()).isEqualTo("changedPassword");
    }

    @DisplayName("유저가 변경하려하는 비밀번호가 이전의 비밀번호와 같을 수 없다.")
    @Test
    void updateUserPassword_Exception() {

        // given
        PasswordUpdateRequestDto dto =
                new PasswordUpdateRequestDto("1234", "1234");

        given(userRepository.findByUserEmail("1234@1234.com")).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches("1234", "1234")).willReturn(true);

        // then
        assertThatThrownBy(
                () -> userService
                        .modifyUserPassword("1234@1234.com", dto)
        )
                .isInstanceOf(InvalidPasswordUpdateException.class)
                .hasMessageContaining("이전과 동일");
    }

    @DisplayName("회원의 닉네임을 수정한다.")
    @Test
    void updateUserNickname_Success() {

        // given
        User expectedUser = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .userEnabled(true)
                .build();

        String newNickname = "nani_inaning";

        given(userRepository.findByUserEmail(user.getUserEmail()))
                .willReturn(Optional.of(user));

        // when
        userService.modifyNickname("1234@1234.com", newNickname);

        // then
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("userIdx")
                .isEqualTo(expectedUser);
    }

    @DisplayName("회원 삭제한다.")
    @Test
    void deleteUser_Success() {
        // when
        userService.deleteUser(user.getUserEmail());

        // then
        then(userRepository).should().deleteByUserEmail("1234@1234.com");
    }
}
