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

    // 이게 맞나 인환이랑 이야기 해보자
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

//    /**
//     * UncheckedException
//     * Amazon S3에서 요청된 객체가 버킷에 없는 경우 오류를 반환합니다.
//     * @return {@link ResponseEntity}를 반환
//     */
//    @ExceptionHandler(AmazonS3Exception.class)
//    protected ResponseEntity<ResponseState> handleAmazonS3Exception() {
//        log.error("handleAmazonS3Exception throw Exception : {}", AMAZONS_S3_EXCEPTION);
//        return ResponseState.toResponseEntity(AMAZONS_S3_EXCEPTION);
//    }

//    @ExceptionHandler(IOException.class)
//    protected ResponseEntity<ResponseState> handleIOException() {
//        log.error("handleIOException throw Exception : {}", INPUT_OUTPUT_EXCEPTION);
//        return ResponseState.toResponseEntity(INPUT_OUTPUT_EXCEPTION);
//    }

//    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
//    protected ResponseEntity<ResponseState> handleDataException() {
//        log.error("handleDataException throw Exception : {}", DATA_VIOLATION_EXCEPTION);
//        return ResponseState.toResponseEntity(DATA_VIOLATION_EXCEPTION);
//    }

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

//        return ResponseEntity
//                .status(e.getHttpStatus())
//                .body(new ApiErrorResponse(errorCode, message));
    }
}
