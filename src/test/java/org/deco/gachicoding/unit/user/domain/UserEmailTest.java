package org.deco.gachicoding.unit.user.domain;


import org.deco.gachicoding.user.domain.UserEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEmailTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("")
    @Test
    public void userEmail_validation_Failed_Blank() {

        // given
        UserEmail email = new UserEmail("");

        // when
        Set<ConstraintViolation<UserEmail>> violations = validator.validate(email);

        // then
        assertThat(violations).isNotEmpty();
        for (ConstraintViolation violation : violations) {
            assertThat(violation.getMessage())
                    .contains("공백");
        }


    }
}
