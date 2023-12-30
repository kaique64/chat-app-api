package io.chat.app.application.exceptions;

import org.springframework.http.HttpStatus;

import java.nio.file.attribute.FileAttribute;

public class AppException extends RuntimeException {

    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return this.status;
    }
}
