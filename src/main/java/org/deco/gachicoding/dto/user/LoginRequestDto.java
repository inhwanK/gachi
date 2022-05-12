package org.deco.gachicoding.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@ApiModel(value = "LoginRequestDto : 로그인 요청 Dto", description = "로그인 요청 Dto")
@Getter
@Setter
public class LoginRequestDto {

    @ApiModelProperty(value = "사용자 ID", required = true, example = "test@test.com")
    @NotNull @Email(message = "올바르지 않은 이메일 형식입니다.")
    private String email;

    @ApiModelProperty(value = "비밀번호", required = true, example = "asdf23145")
    @NotNull
    private String password;
}
