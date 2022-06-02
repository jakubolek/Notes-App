package com.jakubolek.notesapp.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ServiceException extends RuntimeException {

    private HttpStatus httpStatus = INTERNAL_SERVER_ERROR;

    public ServiceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}