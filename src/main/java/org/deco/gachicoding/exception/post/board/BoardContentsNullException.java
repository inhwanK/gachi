package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardContentsNullException extends PostException {

    private static final String ERROR_CODE = "B1005";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "게시물의 내용이 널이어서는 안됩니다.";

    public BoardContentsNullException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardContentsNullException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
