package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardContentsFormatException extends PostException {

    private static final String ERROR_CODE = "B1004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "게시물 내용이 길이 제한을 초과하였습니다.";

    public BoardContentsFormatException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardContentsFormatException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
