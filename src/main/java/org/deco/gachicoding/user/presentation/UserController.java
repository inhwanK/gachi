package org.deco.gachicoding.user.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.user.password.IncorrectPasswordConfirmException;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.PasswordUpdateRequestDto;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Api(tags = "사용자 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "아이디 중복 체크", notes = "아이디로 사용할 이메일의 중복 체크")
    @ApiResponse(code = 200, message = "이메일이 중복일 경우 false, 아닐 경우 true 반환")
    @GetMapping("/user/regist/check-email")
    public Boolean checkEmail(
            @ApiParam(value = "중복 체크 할 이메일")
            @RequestParam("email") String email
    ) {
        return !userRepository.existsByUserEmail(email);
    }


    @ApiOperation(value = "회원가입", notes = "회원가입 수행")
    @ApiResponse(code = 200, message = "회원가입 완료")
    @PostMapping("/user/create")
    public Long registerUser(
            @ApiParam(name = "요청 DTO", value = "회원가입을 위한 요청 body 정보")
            @Valid @RequestBody UserSaveRequestDto dto
    ) {
        return userService.createUser(dto);
    }


    @ApiOperation(value = "유저 닉네임 수정")
    @ApiResponse(code = 200, message = "수정 완료")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/user/update-nickname")
    public ResponseEntity<String> updateUser(
            @ApiParam(value = "변경할 닉네임")
            @RequestParam("newNickname") @NotBlank String newNickname
    ) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String modifiedNickname = userService.modifyNickname(userEmail, newNickname);

        return ResponseEntity.ok(modifiedNickname);
    }

    // 프론트로부터 암호화된 비밀번호가 와야할 것 같은데...
    @ApiOperation(value = "유저 정보 수정 전에 확인하는 api")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/confirm")
    public void confirmUser(
            @ApiParam(value = "비밀번호 변경 전 사용자 확인")
            @RequestParam @NotBlank String confirmPassword
    ) {
        String userPassword =
                (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        boolean correct = userService.confirmUser(confirmPassword, userPassword);
        if(!correct) {
            throw new IncorrectPasswordConfirmException();
        }

    }


    @ApiOperation(value = "유저 비밀번호 변경 api")
    @ApiResponse(code = 200, message = "비밀번호가 변경되었습니다.")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/user/change-password")
    public void updateUserPassword(
            @ApiParam(value = "변경할 비밀번호 요청 body")
            @RequestBody @Valid PasswordUpdateRequestDto dto
    ) {
        // 여기서 dto 안의 두 필드가 같은지 다른지 체크된 상태여야함.

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.modifyUserPassword(userEmail, dto);
        // 리다이렉션 필요?
    }


    @ApiOperation(value = "유저 삭제", notes = "userIdx 값을 받아 유저 삭제 수행, ")
    @ApiResponse(code = 200, message = "사용자 정보 삭제 완료")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER')")
    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser() {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUser(userEmail);

        return ResponseEntity.noContent().build();
    }
}