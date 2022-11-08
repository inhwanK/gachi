package org.deco.gachicoding.exception.post.answer;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class AnswerContentsNullException extends PostException {

    private static final String ERROR_CODE = "A1005";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "답변의 내용이 널이어서는 안됩니다.";

    public AnswerContentsNullException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private AnswerContentsNullException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
