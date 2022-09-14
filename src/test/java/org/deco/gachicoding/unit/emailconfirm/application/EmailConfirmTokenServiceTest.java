package org.deco.gachicoding.unit.emailconfirm.application;

import org.deco.gachicoding.common.factory.user.MockUser;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class})
public class EmailConfirmTokenServiceTest {

    @InjectMocks
    private EmailConfirmTokenService emailConfirmTokenService;

    @Mock
    private EmailConfirmTokenRepository emailConfirmTokenRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private String targetEmail;
    private EmailConfirmToken emailConfirmToken;

    @BeforeEach
    void setUp() {
        targetEmail = "1234@1234.com";

        emailConfirmToken = new EmailConfirmToken(
                UUID.randomUUID(),
                targetEmail,
                LocalDateTime.now().plusMinutes(5L),
                false
        );

        user = MockUser.builder()
                .userEmail("1234@1234.com")
                .build();
    }

    @DisplayName("이메일 인증 토큰을 생성한다.")
    @Test
    void createToken_Success() {

        // given
        given(emailConfirmTokenRepository.existsByTargetEmail(targetEmail))
                .willReturn(false);

        given(emailConfirmTokenRepository.save(any(EmailConfirmToken.class)))
                .willReturn(emailConfirmToken);

        // when
        UUID expectedToken = emailConfirmTokenService.createToken(targetEmail);

        // then
        verify(emailConfirmTokenRepository).save(any(EmailConfirmToken.class));

        assertThat(expectedToken)
                .isEqualTo(emailConfirmToken.getTokenId());
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
        UUID expectedToken = emailConfirmTokenService.createToken(targetEmail);

        // then
        verify(emailConfirmTokenRepository).deleteByTargetEmail(targetEmail);
        verify(emailConfirmTokenRepository).save(any(EmailConfirmToken.class));

        assertThat(expectedToken)
                .isEqualTo(emailConfirmToken.getTokenId());
    }

    @DisplayName("유효한 토큰에 대해 true를 반환한다.")
    @Test
    void checkToken_Return_True() {
        // given
        given(emailConfirmTokenRepository
                .findByTokenId(emailConfirmToken.getTokenId()))
                .willReturn(Optional.of(emailConfirmToken));

        given(userRepository.findByUserEmail(targetEmail))
                .willReturn(Optional.of(user));

        // when
        boolean expected = emailConfirmTokenService.checkToken(
                emailConfirmToken.getTokenId(),
                targetEmail
        );

        // then
        assertThat(expected).isTrue();
    }

    @DisplayName("유효하지 않은 토큰에 대해 false를 반환한다.")
    @Test
    void checkToken_Return_False() {
        // given
        given(emailConfirmTokenRepository
                .findByTokenId(emailConfirmToken.getTokenId()))
                .willReturn(Optional.of(emailConfirmToken));

        given(userRepository.findByUserEmail(targetEmail))
                .willReturn(Optional.of(user));

        // when
        boolean expected = emailConfirmTokenService.checkToken(
                UUID.randomUUID(),
                targetEmail
        );

        // then
        assertThat(expected).isFalse();
    }
}
