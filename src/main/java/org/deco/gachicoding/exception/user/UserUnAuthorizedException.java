package org.deco.gachicoding.exception.user;

import org.springframework.http.HttpStatus;

public class UserUnAuthorizedException extends UserException {

    private static final String ERROR_CODE = "U0002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    private static final String MESSAGE = "권한이 없는 사용자입니다.";

    public UserUnAuthorizedException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    public UserUnAuthorizedException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
