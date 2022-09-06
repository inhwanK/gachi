package org.deco.gachicoding.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PasswordUpdateRequestDto {

    @NotNull
    private String updatePassword;
    @NotNull
    private String verifyPassword;
}
