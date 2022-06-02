package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class UsernameIsTakenException extends ServiceException {
    public UsernameIsTakenException() {
        super(CONFLICT, "This username is already taken.");
    }
}
