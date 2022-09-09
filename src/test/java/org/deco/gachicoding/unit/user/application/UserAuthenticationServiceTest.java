package org.deco.gachicoding.unit.user.application;

import org.deco.gachicoding.common.factory.user.MockUser;
import org.deco.gachicoding.user.application.UserAuthenticationService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.authentication.UserAuthenticationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith({MockitoExtension.class})
public class UserAuthenticationServiceTest {

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("인증 시도하는 사용자 이메일이 존재하면 사용자 정보를 반환한다.")
    @Test
    public void loadUserByUsername_Success() {
        // given
        user = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getUserRole().toString()));
        UserDetails expectedUserDetails = new UserAuthenticationDto(user, roles);

        String userEmail = user.getUserEmail();
        given(userRepository.findByUserEmail(userEmail))
                .willReturn(Optional.of(user));

        // then
        assertThat(userAuthenticationService.loadUserByUsername(userEmail))
                .usingRecursiveComparison()
                .isEqualTo(expectedUserDetails);
    }

    @DisplayName("입력한 이메일이 존재하지 않으면 인증이 되지 않는다.")
    @Test
    public void loadUserByUsername_UsernameNotFoundException() {
        // given
        user = MockUser.builder()
                .userEmail("1234@1234.com")
                .userName("InHwan")
                .userNick("nani_inaning")
                .userPassword("1234")
                .build();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getUserRole().toString()));
        UserDetails expectedUserDetails = new UserAuthenticationDto(user, roles);

        String userEmail = user.getUserEmail();
        given(userRepository.findByUserEmail("invalid Username"))
                .willThrow(new UsernameNotFoundException("Invalid Username"));

        // then
        assertThatThrownBy(
                () -> userAuthenticationService.loadUserByUsername("invalid Username")
        )
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Invalid Username");
    }
}
