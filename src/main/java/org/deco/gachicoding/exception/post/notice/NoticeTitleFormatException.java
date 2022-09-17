package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeTitleFormatException extends PostException {

    private static final String ERROR_CODE = "N1001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "공지사항의 제목이 길이 제한을 초과하였습니다.";

    public NoticeTitleFormatException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeTitleFormatException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
