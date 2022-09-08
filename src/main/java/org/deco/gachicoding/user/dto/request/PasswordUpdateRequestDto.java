package org.deco.gachicoding.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PasswordUpdateRequestDto {

    @ApiModelProperty(value = "변경할 비밀번호", required = true, example = "12345")
    @NotBlank
    private String updatePassword;

    @ApiModelProperty(value = "변경할 비밀번호 확인", required = true, example = "12345")
    @NotBlank
    private String confirmPassword;
}
