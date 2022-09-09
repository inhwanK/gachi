package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardAlreadyActiveException extends PostException {

    private static final String ERROR_CODE = "B0004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 활성화 된 게시물 입니다.";

    public BoardAlreadyActiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardAlreadyActiveException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
