package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class QuestionAlreadyActiveException extends PostException {

    private static final String ERROR_CODE = "Q0004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 활성화 된 질문입니다.";

    public QuestionAlreadyActiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionAlreadyActiveException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
