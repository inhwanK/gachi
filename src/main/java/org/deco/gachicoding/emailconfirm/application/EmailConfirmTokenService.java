package org.deco.gachicoding.emailconfirm.application;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
import org.deco.gachicoding.emailconfirm.domain.repository.EmailConfirmTokenRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailConfirmTokenService {

    private final EmailConfirmTokenRepository emailConfirmTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public EmailConfirmToken createToken(
            String targetEmail
    ) {

        if (emailConfirmTokenRepository.existsByTargetEmail(targetEmail)) {
            emailConfirmTokenRepository.deleteByTargetEmail(targetEmail);
        }

        EmailConfirmToken token = new EmailConfirmToken(targetEmail);
        emailConfirmTokenRepository.save(token);

        return token;
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

        confirmToken.confirm();
        targetUser.enableUser();

        return true;
    }
}
