package org.deco.gachicoding.exception.user.password;

import org.springframework.http.HttpStatus;

public class InvalidPasswordUpdateException extends PasswordException{

    private static final String ERROR_CODE = "P0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public InvalidPasswordUpdateException(String message) {
        this(ERROR_CODE, HTTP_STATUS, message);

    }

    protected InvalidPasswordUpdateException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
