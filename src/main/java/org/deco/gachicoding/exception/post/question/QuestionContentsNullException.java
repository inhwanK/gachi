package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class QuestionContentsNullException extends PostException {

    private static final String ERROR_CODE = "Q1005";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "질문의 내용이 널이어서는 안됩니다.";

    public QuestionContentsNullException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionContentsNullException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
