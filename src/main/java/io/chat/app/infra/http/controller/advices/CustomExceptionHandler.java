package io.chat.app.infra.http.controller.advices;

import io.chat.app.application.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(AppException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", ex.getHttpStatus().value());
        responseBody.put("error", ex.getHttpStatus().name());
        responseBody.put("message", ex.getMessage());

        return new ResponseEntity<>(responseBody, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        HttpStatus statusResponse = HttpStatus.BAD_REQUEST;
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", statusResponse.value());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            String errorMessage = fieldError.getDefaultMessage();
            errors.add(errorMessage);
        }

        responseBody.put("errors", errors);

        return new ResponseEntity<>(responseBody, statusResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
        HttpStatus statusResponse = HttpStatus.UNAUTHORIZED;
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", statusResponse.value());
        responseBody.put("error", statusResponse.name());
        responseBody.put("message", ex.getMessage());

        List<String> errors = new ArrayList<>();

        responseBody.put("errors", errors);

        return new ResponseEntity<>(responseBody, statusResponse);
    }
}