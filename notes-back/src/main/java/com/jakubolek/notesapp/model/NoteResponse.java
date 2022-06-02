package com.jakubolek.notesapp.model;

import lombok.Data;

@Data
public class NoteResponse {
    private long id;
    private String text;

    public NoteResponse(Note note) {
        this.id = note.getId();
        this.text = note.getContent();
    }
}
