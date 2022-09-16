package org.deco.gachicoding.user.dto.request.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@ApiModel(value = "LoginRequestDto : 로그인 요청 Dto", description = "로그인 요청 Dto")
@Getter
public class LoginRequestDto {

    @ApiModelProperty(value = "사용자 ID", required = true, example = "1234@1234.com")
    @JsonProperty("userEmail")
    private String userEmail;

    @ApiModelProperty(value = "비밀번호", required = true, example = "\"1234\"")
    @JsonProperty("password")
    private String password;
}