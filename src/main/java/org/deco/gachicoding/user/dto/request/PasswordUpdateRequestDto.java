package org.deco.gachicoding.user.dto.request;

import lombok.Getter;
import org.deco.gachicoding.exception.user.password.IncorrectPasswordConfirmException;

import javax.validation.constraints.NotBlank;

@Getter
public class PasswordUpdateRequestDto {

    @NotBlank
    private String updatePassword;

    @NotBlank
    private String confirmPassword;

    public PasswordUpdateRequestDto(String updatePassword, String confirmPassword) {
        validate(updatePassword, confirmPassword);

        this.updatePassword = updatePassword;
        this.confirmPassword = confirmPassword;
    }

    private void validate(String updatePassword, String confirmPassword) {
        if (!updatePassword.equals(confirmPassword)) {
            throw new IncorrectPasswordConfirmException();
        }
    }
}
