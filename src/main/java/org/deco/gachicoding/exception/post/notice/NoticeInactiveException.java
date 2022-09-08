package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeInactiveException extends PostException {

    private static final String ERROR_CODE = "N0002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "비활성 처리 된 공지사항입니다.";

    public NoticeInactiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeInactiveException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
