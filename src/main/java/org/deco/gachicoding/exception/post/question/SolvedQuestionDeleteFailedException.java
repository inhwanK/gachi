package org.deco.gachicoding.exception.post.question;

import org.deco.gachicoding.exception.post.PostException;
import org.springframework.http.HttpStatus;

public class SolvedQuestionDeleteFailedException extends PostException {

    private static final String ERROR_CODE = "Q2003";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String MESSAGE = "해결된 질문은 삭제할 수 없습니다.";

    public SolvedQuestionDeleteFailedException() {
        this(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    private SolvedQuestionDeleteFailedException(
            String errorCode,
            HttpStatus httpStatus,
            String message
    ) {
        super(errorCode, httpStatus, message);
    }
}
