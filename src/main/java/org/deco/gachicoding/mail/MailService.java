package org.deco.gachicoding.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final String DOMAIN_URL = "http://localhost:8080";
    private final JavaMailSender mailSender;

    public void sendConfirmToken(String receiver, UUID tokenId) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(receiver);
        mailMessage.setSubject("가치코딩 이메일 인증");
        mailMessage.setText(DOMAIN_URL +
                "/api/confirm?targetEmail=" + receiver +
                "&tokenId=" + tokenId);

        mailSender.send(mailMessage);
    }
}
