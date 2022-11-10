package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeContentsOverMaximumLengthException extends PostException {

    private static final String ERROR_CODE = "N1004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "공지사항의 내용이 길이 제한을 초과하였습니다.";

    public NoticeContentsOverMaximumLengthException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeContentsOverMaximumLengthException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
