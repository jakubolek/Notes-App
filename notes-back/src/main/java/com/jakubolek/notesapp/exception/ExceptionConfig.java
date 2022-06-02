package com.jakubolek.notesapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleException(ServiceException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}

