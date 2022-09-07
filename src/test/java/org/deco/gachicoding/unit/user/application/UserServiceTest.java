package org.deco.gachicoding.unit.user.application;

import org.deco.gachicoding.common.factory.user.MockUser;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.dto.request.UserUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("사용자를 생성한다.")
    @Test
    void createUser_Success() {

        UserSaveRequestDto dto = UserSaveRequestDto.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        User user = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        given(userRepository.existsByUserEmail(any())).willReturn(false);
        given(userRepository.save(any())).willReturn(user);
        given(passwordEncoder.encode(any())).willReturn(any());

        Long userIdx = userService.createUser(dto);
        assertThat(userIdx).isEqualTo(user.getUserIdx());
    }

    @DisplayName("중복된 이메일을 가진 사용자는 회원가입을 할 수 없다.")
    @Test
    void createUser_Exception() {

        UserSaveRequestDto dto = UserSaveRequestDto.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        given(userRepository.existsByUserEmail(any())).willReturn(true);

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("중복된");
    }

    @DisplayName("유저의 비밀번호를 변경한다.")
    @Test
    void updateUserPassword_Success() {

        User user = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        given(userRepository.findByUserEmail("1234@1234.com")).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches("newPassword", "1234")).willReturn(false);
        given(passwordEncoder.encode("newPassword")).willReturn("changedPassword");

        userService.changeUserPassword("1234@1234.com", "newPassword");
        assertThat(user.getUserPassword()).isEqualTo("changedPassword");
    }

    @DisplayName("유저가 변경하려하는 비밀번호가 이전의 비밀번호와 같을 수 없다.")
    @Test
    void updateUserPassword_Exception() {

        User user = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        given(userRepository.findByUserEmail("1234@1234.com")).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches("1234", "1234")).willReturn(true);

        assertThatThrownBy(() -> userService.changeUserPassword("1234@1234.com", "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이전과 동일");
    }

    @DisplayName("회원 정보를 수정한다. (레거시 수정 메서드)")
    @Test
    void updateUser_Legacy_Success() {
        User user = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        User expectedUser = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani")
                .userPassword("newPassword")
                .userLocked(true)
                .userEnabled(true)
                .build();

        UserUpdateRequestDto dto = new UserUpdateRequestDto("nani", "1235", true, true);

        given(userRepository.findByUserEmail(any())).willReturn(Optional.of(user));
        given(passwordEncoder.encode("1235")).willReturn("newPassword");

        userService.updateUser("1234@1234.com", dto);

        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("userIdx")
                .isEqualTo(expectedUser);
    }

    @DisplayName("회원 탈퇴한다.")
    @Test
    void deleteUser_Success() {
        userService.deleteUser(1L);
        then(userRepository).should().deleteById(anyLong());
    }

    @Test
    @DisplayName("UserService - 이메일 형식이 아닌 아이디 회원가입 테스트")
    void notEmailFormatIdJoinUser() {
        fail("미구현");
    }
}
