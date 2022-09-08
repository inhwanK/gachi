package org.deco.gachicoding.exception.post.answer;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AnswerNotFoundException extends ApplicationException {

    private static final String ERROR_CODE = "A0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "해당하는 답변글을 찾을 수 없습니다.";

    public AnswerNotFoundException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private AnswerNotFoundException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
