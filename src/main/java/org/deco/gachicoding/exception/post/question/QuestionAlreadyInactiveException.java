package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class QuestionAlreadyInactiveException extends PostException {

    private static final String ERROR_CODE = "Q0003";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "이미 비활성화 된 공지사항 입니다.";

    public QuestionAlreadyInactiveException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionAlreadyInactiveException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
