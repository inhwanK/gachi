package org.deco.gachicoding.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.auth.Auth;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.dto.social.SocialSaveRequestDto;
import org.deco.gachicoding.dto.user.LoginRequestDto;
import org.deco.gachicoding.dto.user.UserResponseDto;
import org.deco.gachicoding.dto.user.UserSaveRequestDto;
import org.deco.gachicoding.dto.user.UserUpdateRequestDto;
import org.deco.gachicoding.service.SocialService;
import org.deco.gachicoding.service.UserService;
import org.deco.gachicoding.service.impl.AuthService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Api(tags = "사용자 정보 처리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserService userService;
    private final SocialService socialService;
    private final AuthService authService;


    @ApiOperation(value = "이메일 중복 체크", notes = "이메일의 중복을 체크 수행")
    @ApiImplicitParam(name = "email", value = "중복체크 이메일", required = true)
    @ApiResponses(
            @ApiResponse(code = 200, message = "이메일이 중복일 경우 false, 아닐 경우 true 반환")
    )
    @GetMapping("/user/regist/check-email")
    public boolean checkEmail(@ApiParam(name = "email") @RequestParam("email") String email) {
        return !userService.isDuplicatedEmail(email);
    }

    /**
     * 이메일 인증을 위한 토큰 발행
     * @return UUID 토큰
     */
    @ApiOperation(value = "인증 토큰 발행", notes = "이메일 인증을 위한 토큰 생성 후 메일 발송")
    @ApiResponses(
            @ApiResponse(code = 200, message = "인증 메일이 발송되었습니다.")
    )
    @GetMapping("/user/auth-token")
    public UUID sendEmailToken(@ApiParam(value = "인증을 진행할 이메일") @RequestParam String email) {
        return authService.sendEmailConfirmationToken(email);
    }

    /**
     *
     * @param authToken
     * @return userAuth
     */
    @ApiOperation(value = "이메일 인증", notes = "UUID 토큰을 통한 이메일 인증")
    @ApiResponses(
            @ApiResponse(code = 200, message = "이메일 인증이 완료되었습니다.")
    )
    @GetMapping("/user/authentication-email")
    public boolean authenticateEmail(@ApiParam(value = "유저 이메일로 발송된 인증 토큰") @RequestParam UUID authToken) {
        Auth auth = authService.checkToken(authToken);
        Optional<User> user = userService.getUserByUserEmail(auth.getAuthEmail());

        auth.useToken();
        user.get().emailAuthenticated();

        return user.get().isUserAuth();
    }

    @ApiOperation(value = "로그인", notes = "로그인 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "로그인 성공")
    )
    @PostMapping("/user/login")
    public UserResponseDto login(@ApiParam(value = "로그인을 위한 요청 body 정보") @RequestBody LoginRequestDto dto,
                                 @ApiIgnore HttpSession httpSession) throws Exception {

        UserResponseDto userResponseDto = userService.login(dto, httpSession);
        return userResponseDto;
    }

    /**
     * @param httpSession
     * @return UserResponseDto
     * @link Spring Security 를 통한 세션 관리 로직으로 수정해야 함.
     */
    @ApiOperation(value = "유저정보 받기", notes = "세션을 통해 유저정보 전달, 실제로는 HttpSession 클래스를 파라미터로 받음")
    @ApiResponses(
            @ApiResponse(code = 200, message = "세션 정보 가져오기 성공")
    )
    @GetMapping("/user/info")
    public UserResponseDto getUserInfo(@ApiIgnore HttpSession httpSession) {
        UserResponseDto userInfo = (UserResponseDto) httpSession.getAttribute("user");
        return userInfo;
    }

    /**
     * 혹시 세션이 존재하지 않을 경우에 로그아웃 요청이 들어오면, 새롭게 세션을 생성하지 않도록 함.
     * Spring Security 를 통한 세션 관리 로직으로 수정 필요.
     *
     * @return void
     */
    @ApiOperation(value = "로그아웃", notes = "세션 객체 무효화, 실제로는 HttpServletRequest 클래스 정보를 파라미터로 받음")
    @ApiResponses(
            @ApiResponse(code = 200, message = "로그아웃 성공")
    )
    @GetMapping("/user/logout")
    public void logout(@ApiIgnore HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        httpSession.invalidate();
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "회원가입 완료")
    )
    @PostMapping("/user/regist")
    public Long registerUser(@ApiParam(name = "요청 DTO", value = "회원가입을 위한 요청 body 정보") @Valid @RequestBody UserSaveRequestDto dto) {
        return userService.registerUser(dto);
    }

    @ApiOperation(value = "유저 정보 업데이트", notes = "userIdx, UserUpdateRequestDto 를 받아서 유저 업데이트 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "사용자 수정 완료")
    )
    @PutMapping("/user/{userIdx}")
    public Long updateUser(@ApiParam(value = "수정할 유저의 번호") @PathVariable Long userIdx,
                           @ApiParam(value = "사용자 정보 수정을 위한 요청 body 정보") @RequestBody UserUpdateRequestDto dto) {
        return userService.updateUser(userIdx, dto);
    }

    @ApiOperation(value = "유저 삭제", notes = "userIdx 값을 받아 유저 삭제 수행, ")
    @ApiResponses(
            @ApiResponse(code = 200, message = "사용자 정보 삭제 완료")
    )
    @DeleteMapping("/user/{userIdx}")
    public Long deleteUser(@ApiParam(value = "삭제할 사용자의 번호") @PathVariable Long userIdx) {
        return userService.deleteUser(userIdx);
    }

    @ApiOperation(value = "카카오 로그인", notes = "개발 중...")
    @GetMapping("/user/kakaoLogin")
    public void kakaoUserLogin(String code) throws Exception {
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

    }
}