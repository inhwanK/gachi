package org.deco.gachicoding.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ApplicationException extends RuntimeException {
    // 익셉션 코드 추가 리팩토링 필요
    private final String errorCode;
    private final HttpStatus httpStatus;
//    private final StatusEnum statusEnum;

    protected ApplicationException(String errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
