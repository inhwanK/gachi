package org.deco.gachicoding.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * UncheckedException
     * Amazon S3에서 요청된 객체가 버킷에 없는 경우 오류를 반환합니다.
     * @return {@link ResponseEntity}를 반환
     */
    @ExceptionHandler(AmazonS3Exception.class)
    protected ResponseEntity<ResponseState> handleAmazonS3Exception() {
        log.error("handleAmazonS3Exception throw Exception : {}", INPUT_OUTPUT_EXCEPTION);
        return ResponseState.toResponseEntity(INPUT_OUTPUT_EXCEPTION);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ResponseState> handleNullPointerException() {
        log.error("handleNullPointerException throw Exception : {}", NULL_POINTER);
        return ResponseState.toResponseEntity(NULL_POINTER);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ResponseState> handleIOException() {
        log.error("handleIOException throw Exception : {}", INPUT_OUTPUT_EXCEPTION);
        return ResponseState.toResponseEntity(INPUT_OUTPUT_EXCEPTION);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    protected ResponseEntity<ResponseState> handleUnsupportedEncodingException() {
        log.error("handleUnsupportedEncodingException throw Exception : {}", INPUT_OUTPUT_EXCEPTION);
        return ResponseState.toResponseEntity(INPUT_OUTPUT_EXCEPTION);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ResponseState> handleDataException() {
        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
        return ResponseState.toResponseEntity(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ResponseState> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getStatusEnum());
        return ResponseState.toResponseEntity(e.getStatusEnum());
    }
}
