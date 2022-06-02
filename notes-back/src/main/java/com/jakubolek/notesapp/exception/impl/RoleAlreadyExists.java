package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class RoleAlreadyExists extends ServiceException {
    public RoleAlreadyExists(String roleName) {
        super(CONFLICT, "Role " + roleName + " already exist.");
    }
}
