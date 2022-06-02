package com.jakubolek.notesapp.service;

import com.jakubolek.notesapp.model.Note;
import com.jakubolek.notesapp.model.NoteResponse;

import java.util.List;

public interface NoteService {

    Note createNote(String text, String username);

    List<NoteResponse> getNotes(String username);

    void deleteNoteById(long id);

    Note updateNote(long id, String text, String username);
}
