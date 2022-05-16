package org.deco.gachicoding.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.dto.jwt.JwtResponseDto;
import org.deco.gachicoding.dto.social.SocialSaveRequestDto;
import org.deco.gachicoding.dto.user.LoginRequestDto;
import org.deco.gachicoding.dto.user.UserResponseDto;
import org.deco.gachicoding.dto.user.UserSaveRequestDto;
import org.deco.gachicoding.dto.user.UserUpdateRequestDto;
import org.deco.gachicoding.service.SocialService;
import org.deco.gachicoding.service.UserService;
import org.deco.gachicoding.service.impl.UserDetailsImpl;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Api
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserService userService;

    private final SocialService socialService;

    @ApiOperation(value = "로그인", notes = "email, password 값을 받아 로그인 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "accessToken 으로 변조된 로그인 정보 반환")
    )
    @PostMapping("/user/login")
    public String login(@ApiParam(value = "이메일과 비밀번호", required = true) @RequestBody LoginRequestDto dto,
                        @ApiParam(value = "세션을 위한 파라미터", required = false) HttpSession httpSession) throws Exception {

        UserResponseDto userResponseDto = userService.login(dto, httpSession);
        return userResponseDto.getUserEmail();
    }

    /**
     * @param httpSession
     * @return UserResponseDto
     * @link Spring Security 를 통한 세션 관리 로직으로 수정해야 함.
     */
    @ApiModelProperty(hidden = true)
    @GetMapping("/user/info")
    public UserResponseDto getUserInfo(HttpSession httpSession) {
        UserResponseDto userInfo = (UserResponseDto) httpSession.getAttribute("user");
        return userInfo;
    }

    /**
     * @return void
     * @link Spring Security 를 통한 세션 관리 로직으로 수정해야 함.
     */
    @ApiModelProperty(hidden = true)
    @GetMapping("/user/logout")
    public void logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        httpSession.invalidate();
    }

    @ApiOperation(value = "회원가입", notes = "UserSaveRequestDto 타입으로 값을 받아 회원가입 수행")
    @PostMapping("/user")
    public Long registerUser(@Valid @RequestBody UserSaveRequestDto dto) {
        return userService.registerUser(dto);
    }

    @ApiOperation(value = "유저 정보 업데이트", notes = "userIdx, UserUpdateRequestDto 를 받아서 유저 업데이트 수행")
    @PutMapping("/user/{userIdx}")
    public Long updateUser(@PathVariable Long userIdx, @RequestBody UserUpdateRequestDto dto) {
        return userService.updateUser(userIdx, dto);
    }

    @ApiOperation(value = "유저 삭제", notes = "userIdx 값을 받아 유저 삭제 수행, ")
    @DeleteMapping("/user/{userIdx}")
    public Long deleteUser(@PathVariable Long userIdx) {
        return userService.deleteUser(userIdx);
    }

    @ApiOperation(value = "카카오 로그인", notes = "잘 모름..")
    @GetMapping("/user/kakaoLogin")
    public JwtResponseDto kakaoUserLogin(String code) throws Exception {
        System.out.println("kakaoCode" + code);

        Long idx;

        String accessToken = socialService.getKakaoAccessToken(code);
        SocialSaveRequestDto socialSaveRequestDto = socialService.getKakaoUserInfo(accessToken);

        // 회원 확인
        Optional<User> user = userService.getUserByUserEmail(socialSaveRequestDto.getSocialId());

        LoginRequestDto jwtRequestDto = new LoginRequestDto();

        jwtRequestDto.setEmail(socialSaveRequestDto.getSocialId());

        // 카카오 소셜 인증이 없으면
        if (socialService.getSocialTypeAndEmail(socialSaveRequestDto).isEmpty()) {

            // 같은 이메일로 가입된 회원이 없으면
            if (user.isEmpty()) {
                // 유저 회원 가입
                UserSaveRequestDto userSaveRequestDto = UserSaveRequestDto.builder()
                        .userName(socialSaveRequestDto.getUserName())
                        .userEmail(socialSaveRequestDto.getSocialId())
                        .userPassword("a123456789a")    // -> 정해야함 암호화된 문자열을 쓰든, 비밀번호 확인 못하게 고정된 키 값을 만들어 두든
                        .userNick(socialSaveRequestDto.getUserName())   // -> 따로 닉네임을 받든(이쪽이 좋을듯 -> 그럼 null값으로 닉네임 넣어두고 업데이트 하는 형태로 가야할 듯), 초기 닉네임을 이름으로 하든
                        .userPicture("userPicture")     // -> 프로필 사진, 수정해야됨
//                                                        .userRole(UserRole.USER)
                        .build();

                idx = userService.registerUser(userSaveRequestDto);

                jwtRequestDto.setPassword("a123456789a");

                System.out.println("신규 유저 소셜 회원 가입 + 로그인 입니다.");
            } else {
                idx = user.get().getUserIdx();

                jwtRequestDto.setPassword(user.get().getUserPassword());

                System.out.println("기존 유저 소셜 인증 + 로그인 입니다.");
            }
            // 유저 idx를 몰랐기 때문에 지금 set
            socialSaveRequestDto.setUserIdx(idx);
            socialService.registerSocial(socialSaveRequestDto);
        }
        // 있으면 로그인 처리(이메일 만을 사용해야함)
        else {
            jwtRequestDto.setPassword(user.get().getUserPassword());
            System.out.println("기존 회원 로그인 입니다." + user.get().getUserPassword());
        }

        // => email - socialId, password - 유저 검색을 통해 알아야함
//        return userService.login(jwtRequestDto);
        return null;
    }

}