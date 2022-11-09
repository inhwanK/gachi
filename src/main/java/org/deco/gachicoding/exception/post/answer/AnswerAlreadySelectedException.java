package org.deco.gachicoding.exception.post.answer;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class AnswerAlreadySelectedException extends PostException {

    private static final String ERROR_CODE = "A0005";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 채택 된 답변입니다.";

    public AnswerAlreadySelectedException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private AnswerAlreadySelectedException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
