package org.deco.gachicoding.user.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.user.password.IncorrectPasswordConfirmException;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.PasswordUpdateRequestDto;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "아이디 중복 체크")
    @GetMapping("/user/regist/check-email")
    public Boolean checkEmail(
            @RequestParam("email") String email
    ) {
        return !userRepository.existsByUserEmail(email);
    }


    @ApiOperation(value = "회원가입")
    @PostMapping("/user/create")
    public Long registerUser(
            @Valid @RequestBody UserSaveRequestDto dto
    ) {
        return userService.createUser(dto);
    }


    @ApiOperation(value = "유저 닉네임 수정")
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
    @ApiOperation(value = "비밀번호 변경 전 사용자 확인")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/confirm")
    public void confirmUser(
            @RequestParam @NotBlank String confirmPassword
    ) {
        String loginUserPassword =
                (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        boolean correct = userService.confirmUser(confirmPassword, loginUserPassword);
        if (!correct)
            throw new IncorrectPasswordConfirmException();

    }


    @ApiOperation(value = "유저 비밀번호 변경 api")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/user/change-password")
    public void updateUserPassword(
            @ApiParam(value = "변경할 비밀번호 요청 body")
            @RequestBody @Valid PasswordUpdateRequestDto dto
    ) {

        String loginUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.modifyUserPassword(loginUserEmail, dto);
    }


    @ApiOperation(value = "유저 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/user")
    public void deleteUser() {

        String loginUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUser(loginUserEmail);
    }
}