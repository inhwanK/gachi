package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class QuestionContentsFormatException extends PostException {

    private static final String ERROR_CODE = "Q1004";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "질문의 내용이 길이 제한을 초과하였습니다.";

    public QuestionContentsFormatException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private QuestionContentsFormatException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
