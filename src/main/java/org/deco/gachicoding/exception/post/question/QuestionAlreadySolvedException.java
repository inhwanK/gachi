package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class QuestionAlreadySolvedException extends PostException {

    private static final String ERROR_CODE = "Q0005";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 해결 된 질문입니다.";

    public QuestionAlreadySolvedException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionAlreadySolvedException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
