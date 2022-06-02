package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class MissingPermissionException extends ServiceException {
    public MissingPermissionException() {
        super(FORBIDDEN, "You don`t have permission to this action.");
    }
}
