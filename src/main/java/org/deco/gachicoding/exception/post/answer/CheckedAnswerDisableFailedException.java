package org.deco.gachicoding.exception.post.answer;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class CheckedAnswerDisableFailedException extends PostException {

    private static final String ERROR_CODE = "A2002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "채택된 답변은 비활성화 할 수 없습니다.";

    public CheckedAnswerDisableFailedException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private CheckedAnswerDisableFailedException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
