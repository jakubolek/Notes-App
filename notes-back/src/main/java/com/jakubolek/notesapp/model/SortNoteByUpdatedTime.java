package com.jakubolek.notesapp.model;

import java.util.Comparator;

public class SortNoteByUpdatedTime implements Comparator<Note> {

    @Override
    public int compare(Note note1, Note note2) {
        return note2.getUpdatedAt().compareTo(note1.getUpdatedAt());
    }
}
