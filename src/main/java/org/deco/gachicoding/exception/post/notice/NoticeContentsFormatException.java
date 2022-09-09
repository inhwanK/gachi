package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeContentsFormatException extends PostException {

    private static final String ERROR_CODE = "N1004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "공지사항 내용의 길이가 제한을 넘었습니다.";

    public NoticeContentsFormatException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeContentsFormatException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
