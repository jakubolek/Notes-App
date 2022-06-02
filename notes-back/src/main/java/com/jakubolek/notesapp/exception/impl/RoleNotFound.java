package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class RoleNotFound extends ServiceException {
    public RoleNotFound(String role) {
        super(NOT_FOUND, "Role: \"" + role + "\" not found.");
    }
}
