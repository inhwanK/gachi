package org.deco.gachicoding.exception.file;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class S3CopyException extends ApplicationException {

    private static final String ERROR_CODE = "I0005";
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE = "S3 이미지 복사 실패";

    public S3CopyException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }
}
