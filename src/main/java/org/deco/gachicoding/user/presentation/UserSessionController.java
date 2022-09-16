package org.deco.gachicoding.user.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.dto.request.authentication.LoginRequestDto;
import org.deco.gachicoding.user.dto.request.authentication.UserAuthenticationDto;
import org.deco.gachicoding.user.dto.response.UserResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "사용자 세션 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserSessionController {

    @ApiOperation(value = "유저정보 받기", notes = "임시로 만든 API, 인가 처리 개발되는데로 삭제 예정")
    @ApiResponses(
            @ApiResponse(code = 200, message = "세션 정보 가져오기 성공")
    )
    @GetMapping("/user/info")
    public UserResponseDto getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthenticationDto dto = (UserAuthenticationDto) authentication.getPrincipal();
        UserResponseDto responseDto = new UserResponseDto(dto.getUsername(), dto.getUserNick());
        return responseDto;
    }

    @ApiOperation(value = "로그인 요청", notes = "로그인 요청 api 문서")
    @PostMapping("/login")
    public void login(
            @ApiParam(name = "로그인 요청 dto")
            @Valid @RequestBody LoginRequestDto dto
    ) {
    }

    @ApiOperation(value = "로그아웃 요청", notes = "로그아웃 요청 api 문서")
    @PostMapping("/logout")
    public void logout() {}
}
