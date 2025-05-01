package com.JPA.redis.Exception;

import com.JPA.redis.DTO.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = WebException.class)
    ResponseEntity<ApiResponse> handlerRunTimeException(WebException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity.badRequest()
                .body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage())

                        .build()

                );
    }
}
