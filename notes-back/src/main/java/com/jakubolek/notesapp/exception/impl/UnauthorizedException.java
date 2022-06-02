package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException() {
        super(UNAUTHORIZED, "Your authorization failed.");
    }
}
