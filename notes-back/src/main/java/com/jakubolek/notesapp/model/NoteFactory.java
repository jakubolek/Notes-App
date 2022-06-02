package com.jakubolek.notesapp.model;

public class NoteFactory {

    public static Note create(String content, User username) {
        return Note.builder()
                .content(content)
                .username(username)
                .build();
    }
}
