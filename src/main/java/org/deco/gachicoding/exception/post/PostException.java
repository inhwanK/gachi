package org.deco.gachicoding.exception.post;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PostException extends ApplicationException {

    public PostException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
