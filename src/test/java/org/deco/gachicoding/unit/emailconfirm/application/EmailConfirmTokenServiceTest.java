package org.deco.gachicoding.unit.emailconfirm.application;

import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.emailconfirm.application.EmailConfirmTokenService;
import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
import org.deco.gachicoding.emailconfirm.domain.repository.EmailConfirmTokenRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith({MockitoExtension.class})
public class EmailConfirmTokenServiceTest {

    @InjectMocks
    private EmailConfirmTokenService emailConfirmTokenService;

    @Spy
    private EmailConfirmTokenRepository emailConfirmTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailConfirmToken emailConfirmToken;


    private User user;
    private String targetEmail;

    @BeforeEach
    void setUp() {
        targetEmail = "1234@1234.com";

        emailConfirmToken = new EmailConfirmToken(targetEmail);

        user = UserMock.builder()
                .userEmail("1234@1234.com")
                .build();
    }

    @DisplayName("이메일 인증 토큰을 생성한다.")
    @Test
    void createToken_Success() {

        // given
        given(emailConfirmTokenRepository.existsByTargetEmail(targetEmail))
                .willReturn(false);

        // when
        EmailConfirmToken expectedToken = emailConfirmTokenService.createToken(targetEmail);

        // then
        verify(emailConfirmTokenRepository).save(any(EmailConfirmToken.class));

        assertThat(expectedToken)
                .isInstanceOf(EmailConfirmToken.class);
    }

    @DisplayName("이미 존재하는 인증 토큰을 삭제하고 새 토큰을 생성한다.")
    @Test
    void createToken_Success_2() {

        // given

        given(emailConfirmTokenRepository.existsByTargetEmail(targetEmail))
                .willReturn(true);
        given(emailConfirmTokenRepository.save(any(EmailConfirmToken.class)))
                .willReturn(emailConfirmToken);

        // when
        EmailConfirmToken expectedToken = emailConfirmTokenService.createToken(targetEmail);

        // then
        verify(emailConfirmTokenRepository).deleteByTargetEmail(targetEmail);
        verify(emailConfirmTokenRepository).save(any(EmailConfirmToken.class));

        assertThat(expectedToken)
                .isInstanceOf(EmailConfirmToken.class);
    }

    @DisplayName("유효한 토큰에 대해 true를 반환한다.")
    @Test
    void checkToken_Return_True() {
        // given
        emailConfirmToken.setTokenId(UUID.randomUUID());

        given(emailConfirmTokenRepository.findByTokenId(any(UUID.class)))
                .willReturn(Optional.of(emailConfirmToken));

        given(userRepository.findByUserEmail(targetEmail))
                .willReturn(Optional.of(user));

        // when
        boolean expected = emailConfirmTokenService.checkToken(
                emailConfirmToken.getTokenId(),
                targetEmail
        );

        // then
        assertThat(user.isUserEnabled()).isTrue();
        assertThat(emailConfirmToken.isConfirmed())
                .isTrue();

        assertThat(expected).isTrue();

    }

//    @DisplayName("인증을 시도한 토큰이 만료되었을 경우 false를 반환한다.")
//    @Test
    void checkToken_Return_False() {
        // given
        emailConfirmToken.setTokenId(UUID.randomUUID());

        given(emailConfirmTokenRepository.findByTokenId(any(UUID.class)))
                .willReturn(Optional.of(emailConfirmToken));

        given(userRepository.findByUserEmail(targetEmail))
                .willReturn(Optional.of(user));

        // when
        boolean expected = emailConfirmTokenService.checkToken(
                emailConfirmToken.getTokenId(),
                targetEmail
        );

        // then
        assertThat(user.isUserEnabled()).isTrue();
        assertThat(emailConfirmToken.isConfirmed())
                .isTrue();

        assertThat(expected).isTrue();
        fail("미구현");
    }


    @DisplayName("인증을 시도하는 토큰의 tokenId를 가진 토큰이 존재하지 않으면 예외를 던진다.")
    @Test
    void checkToken_Throw_Exception() {

        // given
        UUID unmatched = UUID.randomUUID();

        // when
        assertThatThrownBy(
                () -> emailConfirmTokenService.checkToken(unmatched, targetEmail)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 코드");
    }
}
