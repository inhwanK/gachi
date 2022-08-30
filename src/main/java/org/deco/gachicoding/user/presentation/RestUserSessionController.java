package org.deco.gachicoding.user.presentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.dto.request.UserAuthenticationDto;
import org.deco.gachicoding.user.dto.response.UserResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "사용자 세션 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestUserSessionController {

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
}
