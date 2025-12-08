package com.laundryheroes.core.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ResponseFactory responseFactory;

    public GlobalExceptionHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument", ex);
        ApiResponse<?> body = responseFactory.error(ResponseCode.INVALID_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Validation error", ex);
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::format)
                .reduce("", (a, b) -> a.isEmpty() ? b : a + "; " + b);

        ApiResponse<?> body = responseFactory.error(ResponseCode.INVALID_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private static final org.slf4j.Logger log =
        org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

@ExceptionHandler(Exception.class)
public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
    log.error("Unhandled exception caught by GlobalExceptionHandler", ex);

    ApiResponse<?> body = responseFactory.error(ResponseCode.FAILURE, ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
}

    private String format(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
