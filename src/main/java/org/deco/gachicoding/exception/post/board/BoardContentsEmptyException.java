package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardContentsEmptyException extends PostException {

    private static final String ERROR_CODE = "B1006";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "게시물의 내용이 공백이어서는 안됩니다.";

    public BoardContentsEmptyException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardContentsEmptyException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
