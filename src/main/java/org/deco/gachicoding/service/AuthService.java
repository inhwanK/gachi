package org.deco.gachicoding.service;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.auth.Auth;
import org.deco.gachicoding.domain.auth.AuthRepository;
import org.deco.gachicoding.service.MailService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final MailService mailService;

    /**
     * 이메일 인증 토큰 생성
     *
     * @return
     */
    public UUID sendEmailConfirmationToken(String receiverEmail) {

        Optional<Auth> auth = authRepository.findByAuthEmailAndAuthExpdateAfterAndExpiredIsFalse(receiverEmail, LocalDateTime.now());
        Assert.hasText(receiverEmail, "receiverEmail은 필수 입니다.");

        UUID authToken;
        if (auth.isEmpty())
            authToken = authRepository.save(Auth.createEmailConfirmationToken(receiverEmail)).getAuthToken();
        else
            authToken = auth.get().renewToken();

        mailService.sendConfirmMail(receiverEmail, authToken);
        return authToken;
    }

    // 만료 또는 인증된 토큰 처리 필요
    public Auth checkToken(UUID authToken) {
        Optional<Auth> auth = authRepository.findByAuthToken(authToken);
        return auth.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
    }
}
