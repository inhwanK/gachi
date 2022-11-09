package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class QuestionTitleNullException extends PostException {

    private static final String ERROR_CODE = "Q1002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "질문의 제목이 널이어서는 안됩니다.";

    public QuestionTitleNullException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionTitleNullException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
