package org.deco.gachicoding.unit.emailconfirm.application;

import org.deco.gachicoding.emailconfirm.application.EmailConfirmTokenService;
import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
import org.deco.gachicoding.emailconfirm.domain.repository.EmailConfirmTokenRepository;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("이메일 인증 토큰을 생성한다.")
    @Test
    void createToken_Success() {

        // given
        String targetEmail = "1234@1234.com";
        EmailConfirmToken token = EmailConfirmToken.createEmailConfirmToken(targetEmail);

        given(emailConfirmTokenRepository.existsByTargetEmail(targetEmail)).willReturn(false);
        given(emailConfirmTokenRepository.save(any(EmailConfirmToken.class))).willReturn(token);

        // when
        UUID expectedToken = emailConfirmTokenService.createToken(targetEmail);

        // then
        verify(emailConfirmTokenRepository).save(any(EmailConfirmToken.class));

        assertThat(expectedToken)
                .isEqualTo(token.getTokenId());
    }

    @DisplayName("이미 존재하는 인증 토큰을 삭제하고 새 토큰을 생성한다.")
    @Test
    void createToken_Success_2() {

        // given
        String targetEmail = "1234@1234.com";
        EmailConfirmToken token = EmailConfirmToken.createEmailConfirmToken(targetEmail);

        given(emailConfirmTokenRepository.existsByTargetEmail(targetEmail)).willReturn(true);
        given(emailConfirmTokenRepository.save(any(EmailConfirmToken.class))).willReturn(token);

        // when
        UUID expectedToken = emailConfirmTokenService.createToken(targetEmail);

        // then
        verify(emailConfirmTokenRepository).save(any(EmailConfirmToken.class));

        assertThat(expectedToken)
                .isEqualTo(token.getTokenId());
    }

    @Test
    void checkToken() {
        fail("미구현");
    }
}
