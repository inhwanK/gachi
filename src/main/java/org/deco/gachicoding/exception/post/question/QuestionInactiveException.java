package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class QuestionInactiveException extends PostException {

    private static final String ERROR_CODE = "Q0002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "비활성 처리 된 질문글입니다.";

    public QuestionInactiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionInactiveException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
