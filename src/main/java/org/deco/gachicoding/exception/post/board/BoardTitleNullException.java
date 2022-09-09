package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardTitleNullException extends PostException {

    private static final String ERROR_CODE = "B1002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "게시물의 제목이 널이어서는 안됩니다.";

    public BoardTitleNullException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardTitleNullException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
