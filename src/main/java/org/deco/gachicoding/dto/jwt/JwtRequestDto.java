package org.deco.gachicoding.dto.jwt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "JwtRequestDto : 로그인 요청 Dto", description = "Jwt를 이용한 로그인 요청 Dto")
@Getter
@Setter
public class JwtRequestDto {

    @ApiModelProperty(value = "사용자 ID", required = true, example = "test@test.com")
    private String email;

    @ApiModelProperty(value = "비밀번호", required = true, example = "asdf23145")
    private String password;
}
