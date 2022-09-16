package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeAlreadyInactiveException extends PostException {

    private static final String ERROR_CODE = "N0003";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 비활성화 된 공지사항 입니다.";

    public NoticeAlreadyInactiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeAlreadyInactiveException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
