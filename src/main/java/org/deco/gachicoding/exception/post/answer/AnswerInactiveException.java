package org.deco.gachicoding.exception.post.answer;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class AnswerInactiveException extends PostException {

    private static final String ERROR_CODE = "A0002";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "비활성 처리 된 답변입니다.";

    public AnswerInactiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private AnswerInactiveException(
            String errorCode,
            HttpStatus httpStatus,
            String Message
    ) {
        super(errorCode, httpStatus, Message);
    }
}
