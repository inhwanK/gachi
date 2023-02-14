package org.deco.gachicoding.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    // 익셉션 메세지를 response에 넣을때 좀더 디테일 하게 넣을 수 있도록 공부해 봅시다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
            MethodArgumentNotValidException e
    ) {
        String field = e.getFieldError().getField();
        String errorCode = e.getFieldError().getDefaultMessage();

        ExceptionEnum ee = ExceptionEnum.valueOf(errorCode);

        String errorMessage = field + " - " + ee.getDetail();

        log.warn(
                LOG_FORMAT,
                e.getObjectName(),
                errorCode,
                errorMessage
        );

        return ExceptionResponse.toResponseEntity(
                new ApplicationException(
                        errorCode,
                        HttpStatus.BAD_REQUEST,
                        errorMessage
                )
        );
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ExceptionResponse> handleCustomException(ApplicationException e) {

        String errorCode = e.getErrorCode();
        String message = e.getMessage();
        log.warn(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                errorCode,
                message
        );

        return ExceptionResponse.toResponseEntity(e);
    }
}
