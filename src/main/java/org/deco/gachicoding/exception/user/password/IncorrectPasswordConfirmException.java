package org.deco.gachicoding.exception.user.password;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordConfirmException extends PasswordException{
    private static final String ERROR_CODE = "P0002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "비밀번호 확인란에 비밀번호와 동일한 값을 입력해주세요.";

    public IncorrectPasswordConfirmException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    public IncorrectPasswordConfirmException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
