package org.deco.gachicoding.exception.file;

import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UtfDecodingException extends ApplicationException {

    private static final String ERROR_CODE = "I0004";
    // 상태 코드 다시 점검
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "UTF8 디코딩 실패";

    public UtfDecodingException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }
}
