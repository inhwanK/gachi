package org.deco.gachicoding.emailconfirm.presentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.emailconfirm.application.EmailConfirmTokenService;
import org.deco.gachicoding.emailconfirm.application.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Api(tags = "이메일 인증 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EmailConfirmController {

    private final EmailConfirmTokenService emailConfirmTokenService;
    private final MailService mailService;

    @ApiOperation(value = "이메일 인증 토큰 발행", notes = "이메일 인증을 위한 토큰 생성 후 메일 발송")
    @ApiResponse(code = 200, message = "인증 메일이 발송되었습니다.")
    @GetMapping("/confirm/send")
    public void sendEmailToken(
            @ApiParam(value = "인증을 진행할 이메일")
            @RequestParam @Email String email
    ) {
        UUID tokenId = emailConfirmTokenService.createToken(email).getTokenId();

        log.info("이메일 토큰 - {}", tokenId);

        mailService.sendConfirmToken(email, tokenId);
    }

    @ApiOperation(value = "이메일 인증", notes = "UUID 토큰을 통한 이메일 인증")
    @ApiResponse(code = 200, message = "이메일 인증이 완료되었습니다.")
    @GetMapping("/confirm")
    public boolean authenticateEmail(
            @ApiParam(value = "유저 이메일로 발송된 인증 토큰")
            @RequestParam @NotNull UUID tokenId,
            @ApiParam(value = "유저 이메일")
            @RequestParam @Email String targetEmail
    ) {

        return emailConfirmTokenService.checkToken(tokenId, targetEmail);
    }
}
