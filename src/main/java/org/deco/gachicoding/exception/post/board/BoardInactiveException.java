package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardInactiveException extends PostException {

    private static final String ERROR_CODE = "B0002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "비활성 처리 된 게시물입니다.";

    public BoardInactiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardInactiveException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
