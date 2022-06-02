package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MissingElementInRequestBodyException extends ServiceException {
    public MissingElementInRequestBodyException() {
        super(BAD_REQUEST, "Incorrect request body.");
    }
}
