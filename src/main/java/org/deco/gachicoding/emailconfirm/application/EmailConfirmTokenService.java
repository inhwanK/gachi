package org.deco.gachicoding.emailconfirm.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
import org.deco.gachicoding.emailconfirm.domain.repository.EmailConfirmTokenRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConfirmTokenService {

    private final EmailConfirmTokenRepository emailConfirmTokenRepository;
    private final UserRepository userRepository;

    @Transactional // 책임이 제대로 분리되지 않은 것 같음....
    public UUID createOrRenewToken(
            String targetEmail
    ) {

        Optional<EmailConfirmToken> confirmToken =
                emailConfirmTokenRepository.retrieveValidToken(targetEmail);

        UUID tokenId;
        if (confirmToken.isPresent()) {
            tokenId = confirmToken.get().renewToken();
        } else {
            EmailConfirmToken token =
                    EmailConfirmToken.createEmailConfirmToken(targetEmail);
            tokenId = token.getTokenId();
            emailConfirmTokenRepository.save(token);
        }

        return tokenId;
    }

    // 만료 또는 인증된 토큰 처리 필요
    @Transactional
    public boolean checkToken(
            UUID tokenId,
            String targetEmail
    ) {

        Optional<EmailConfirmToken> token =
                emailConfirmTokenRepository.retrieveValidToken(targetEmail);

        if (token.isEmpty()) {
            log.info("토큰 기간이 만료되었다는 예외 던지기");
            return false;
        }

        User targetUser = userRepository.findByUserEmail(targetEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (token.get().validCheck(tokenId)) {
            token.get().confirmToken();
            targetUser.enable();
            return true;
        }

        return false;
    }
}
