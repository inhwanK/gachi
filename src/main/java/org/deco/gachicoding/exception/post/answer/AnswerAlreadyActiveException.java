package org.deco.gachicoding.exception.post.answer;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class AnswerAlreadyActiveException extends PostException {

    private static final String ERROR_CODE = "A0004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 활성화 된 답변입니다.";

    public AnswerAlreadyActiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private AnswerAlreadyActiveException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
