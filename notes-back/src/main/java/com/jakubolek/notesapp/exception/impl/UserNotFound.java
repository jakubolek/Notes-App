package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UserNotFound extends ServiceException {
    public UserNotFound(String username) {
        super(NOT_FOUND, "User: \"" + username + "\" not found.");
    }
}
