package org.deco.gachicoding.exception.post.answer;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class CheckedAnswerModifyFailedException extends PostException {

    private static final String ERROR_CODE = "A2001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "채택된 답변은 수정할 수 없습니다.";

    public CheckedAnswerModifyFailedException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private CheckedAnswerModifyFailedException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
