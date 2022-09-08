package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardTitleEmptyException extends PostException {

    private static final String ERROR_CODE = "B1003";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "게시물의 제목이 공백이어서는 안됩니다.";

    public BoardTitleEmptyException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardTitleEmptyException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
