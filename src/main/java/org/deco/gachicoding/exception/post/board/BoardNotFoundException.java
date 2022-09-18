package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends PostException {

    private static final String ERROR_CODE = "B0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "해당하는 게시물을 찾을 수 없습니다.";

    public BoardNotFoundException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardNotFoundException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
