package org.deco.gachicoding.exception.post.notice;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class NoticeNotFoundException extends PostException {

    private static final String ERROR_CODE = "N0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "해당하는 공지사항을 찾을 수 없습니다.";

    public NoticeNotFoundException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private NoticeNotFoundException(
            String ERROR_CODE,
            HttpStatus HTTP_STATUS,
            String MESSAGE
    ) {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }
}
