package org.deco.gachicoding.user.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.application.EmailConfirmService;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "이메일 인증 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EmailConfirmController {

    private final UserRepository userRepository;
    private final EmailConfirmService emailConfirmService;
/*
    @ApiOperation(value = "이메일 인증 토큰 발행", notes = "이메일 인증을 위한 토큰 생성 후 메일 발송")
    @ApiResponse(code = 200, message = "인증 메일이 발송되었습니다.")
    @GetMapping("/user/auth-token")
    public UUID sendEmailToken(
            @ApiParam(value = "인증을 진행할 이메일")
            @RequestParam String email
    ) {
        return emailConfirmService.sendEmailConfirmationToken(email);
    }

    @ApiOperation(value = "이메일 인증", notes = "UUID 토큰을 통한 이메일 인증")
    @ApiResponse(code = 200, message = "이메일 인증이 완료되었습니다.")
    @Transactional // 이게 말이 돼? ㅋㅋㅋ
    @GetMapping("/user/authentication-email")
    public boolean authenticateEmail(
            @ApiParam(value = "유저 이메일로 발송된 인증 토큰")
            @RequestParam UUID authToken
    ) {
        Auth auth = emailConfirmService.checkToken(authToken);
        Optional<User> user = userRepository.findByUserEmail(auth.getAuthEmail());

        auth.useToken();
        user.get().enableUser();

        return user.get().isUserEnabled();
    }

 */
}
