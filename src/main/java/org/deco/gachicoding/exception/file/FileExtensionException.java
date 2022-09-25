package org.deco.gachicoding.exception.file;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FileExtensionException extends ApplicationException {

    private static final String ERROR_CODE = "I0003";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "파일 확장자 에러";

    public FileExtensionException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }
}
