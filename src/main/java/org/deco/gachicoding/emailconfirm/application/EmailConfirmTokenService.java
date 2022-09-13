package org.deco.gachicoding.emailconfirm.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
import org.deco.gachicoding.emailconfirm.domain.repository.EmailConfirmTokenRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        if (emailConfirmTokenRepository.existsByTargetEmail(targetEmail)) {
            emailConfirmTokenRepository.deleteByTargetEmail(targetEmail);
        }

        EmailConfirmToken token = EmailConfirmToken.createEmailConfirmToken(targetEmail);
        emailConfirmTokenRepository.save(token);

        return token.getTokenId();
    }

    @Transactional
    public boolean checkToken(
            UUID tokenId,
            String targetEmail
    ) {

        EmailConfirmToken confirmToken = emailConfirmTokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 코드입니다."));

        User targetUser = userRepository.findByUserEmail(targetEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!confirmToken.validCheck(tokenId)) {
            return false;
        }

        confirmToken.confirmToken();
        targetUser.enableUser();

        return false;
    }
}
