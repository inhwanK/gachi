package org.deco.gachicoding.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

// https://dev-monkey-dugi.tistory.com/51
// 구글 SMTP : https://support.google.com/a/answer/176600?hl=ko#zippy=%2Cgmail-smtp-%EC%84%9C%EB%B2%84-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendConfirmMail(String receiverEmail, UUID authToken) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("http://localhost:8080/api/user/authentication-email?authToken=" + authToken);

        mailSender.send(mailMessage);
    }
}
