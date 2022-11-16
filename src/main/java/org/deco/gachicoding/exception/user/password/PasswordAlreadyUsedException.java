package org.deco.gachicoding.exception.user.password;

import org.springframework.http.HttpStatus;

public class PasswordAlreadyUsedException extends PasswordException{

    private static final String ERROR_CODE = "P0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "비밀번호가 이전과 동일합니다. 새로운 비밀번호를 입력해주세요.";

    public PasswordAlreadyUsedException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    protected PasswordAlreadyUsedException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
