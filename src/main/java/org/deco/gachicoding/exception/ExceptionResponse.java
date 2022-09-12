package org.deco.gachicoding.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private final LocalDateTime Date = LocalDateTime.now();
    private final int statusCode;
    private final String statusMessage;
    private final String errorCode;
    private final String errorMessage;

    public static ResponseEntity<ExceptionResponse> toResponseEntity(ApplicationException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .statusCode(exception.getHttpStatus().value())
                        .statusMessage(exception.getHttpStatus().name())
                        .errorCode(exception.getErrorCode())
                        .errorMessage(exception.getMessage())
                        .build());
    }
}
