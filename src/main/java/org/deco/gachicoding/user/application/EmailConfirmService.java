package org.deco.gachicoding.user.application;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.auth.ConfirmTokenRepository;
import org.deco.gachicoding.mail.MailService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailConfirmService {

    private final ConfirmTokenRepository confirmTokenRepository;
    private final MailService mailService;

    public UUID sendEmailConfirmToken(String targetMail) {

//        boolean isExisted = confirmTokenRepository.existsConfirmToken(targetMail);
//
//        if(isExisted) {
//            UUID confirmToken = confirmTokenRepository.save()
//        }
//
//        mailService.sendConfirmMail(receiverEmail, authToken);
//        return authToken;
        return null;
    }

    // 만료 또는 인증된 토큰 처리 필요
//    public Auth checkToken(UUID authToken) {
//        Optional<Auth> auth = confirmTokenRepository.findByAuthToken(authToken);
//        return auth.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
//    }
}
