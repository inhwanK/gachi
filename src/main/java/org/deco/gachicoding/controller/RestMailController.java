package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "메일 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestMailController {
    private final MailService mailService;

    @ApiOperation(value = "이메일 인증", notes = "개발 중...")
    @ApiResponses(
            @ApiResponse(code = 200, message = "이메일 전송 성공, 수신자 이메일 반환")
    )
    @GetMapping("/mail/send")
    public String sendMailTest(){
        mailService.sendMail();
        return "inhan1009@naver.com";
   }
}
