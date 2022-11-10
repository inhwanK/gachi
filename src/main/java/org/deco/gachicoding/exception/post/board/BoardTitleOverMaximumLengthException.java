package org.deco.gachicoding.exception.post.board;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class BoardTitleOverMaximumLengthException extends PostException {

    private static final String ERROR_CODE = "B1001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "게시물 제목이 길이 제한을 초과하였습니다.";

    public BoardTitleOverMaximumLengthException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private BoardTitleOverMaximumLengthException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
