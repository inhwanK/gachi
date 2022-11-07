package org.deco.gachicoding.exception.file;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends ApplicationException {

    private static final String ERROR_CODE = "F0001";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "해당 파일을 찾을 수 없습니다.";

    public FileNotFoundException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }
}
