package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeTitleEmptyException extends PostException {

    private static final String ERROR_CODE = "N1003";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "공지사항의 제목이 공백이어서는 안됩니다.";

    public NoticeTitleEmptyException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeTitleEmptyException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
