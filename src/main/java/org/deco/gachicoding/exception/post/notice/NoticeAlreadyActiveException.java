package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeAlreadyActiveException extends PostException {

    private static final String ERROR_CODE = "N0004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 활성화 된 공지사항 입니다.";

    public NoticeAlreadyActiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeAlreadyActiveException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
