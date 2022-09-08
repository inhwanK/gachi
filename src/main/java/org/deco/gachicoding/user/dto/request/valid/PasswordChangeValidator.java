package org.deco.gachicoding.user.dto.request.valid;

import org.deco.gachicoding.user.dto.request.PasswordUpdateRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordChangeValidator implements ConstraintValidator<PasswordChangeConstraint, PasswordUpdateRequestDto> {
    @Override
    public void initialize(PasswordChangeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PasswordUpdateRequestDto value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation(); // 기본 메시지 제거

        if(!value.getUpdatePassword().equals(value.getConfirmPassword())){
            context
                    .buildConstraintViolationWithTemplate("수정할 비밀번호와 같은 값을 입력하세요.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
