package org.deco.gachicoding.exception.comment;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CommentException extends ApplicationException {

    public CommentException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
