package com.jakubolek.notesapp.exception.impl;

import com.jakubolek.notesapp.exception.ServiceException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NoteDoesNotExist extends ServiceException {
    public NoteDoesNotExist(long id) {
        super(NOT_FOUND, "Note " + id + " does not exist.");
    }
}
