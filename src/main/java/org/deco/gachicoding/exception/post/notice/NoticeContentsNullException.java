package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeContentsNullException extends PostException {

    private static final String ERROR_CODE = "N1005";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "공지사항의 내용이 널이어서는 안됩니다.";

    public NoticeContentsNullException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeContentsNullException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
