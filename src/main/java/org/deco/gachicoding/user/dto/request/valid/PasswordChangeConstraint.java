package org.deco.gachicoding.user.dto.request.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordChangeValidator.class)
public @interface PasswordChangeConstraint {

    String message() default "{org.deco.gachicoding.user.dto.request.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
