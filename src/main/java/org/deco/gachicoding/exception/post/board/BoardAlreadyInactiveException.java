package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardAlreadyInactiveException extends PostException {

    private static final String ERROR_CODE = "B0003";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 비활성화 된 게시물 입니다.";

    public BoardAlreadyInactiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardAlreadyInactiveException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
