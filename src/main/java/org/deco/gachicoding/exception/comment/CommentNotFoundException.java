package org.deco.gachicoding.exception.comment;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends CommentException {

    private static final String ERROR_CODE = "C0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "해당하는 댓글을 찾을 수 없습니다.";

    public CommentNotFoundException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private CommentNotFoundException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
