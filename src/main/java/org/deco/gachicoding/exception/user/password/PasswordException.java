package org.deco.gachicoding.exception.user.password;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PasswordException extends ApplicationException {

    protected PasswordException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
