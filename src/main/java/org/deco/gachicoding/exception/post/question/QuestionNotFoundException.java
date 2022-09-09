package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends ApplicationException {

    private static final String ERROR_CODE = "Q0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "해당하는 질문글을 찾을 수 없습니다.";

    public QuestionNotFoundException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionNotFoundException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
