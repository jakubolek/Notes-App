package com.jakubolek.notesapp.controller;

import com.jakubolek.notesapp.model.Note;
import com.jakubolek.notesapp.model.NoteResponse;
import com.jakubolek.notesapp.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NotesController {

    private final NoteService noteService;

    @GetMapping
    public List<NoteResponse> getNotes(Principal principal) {
        return noteService.getNotes(principal.getName());
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Note addNote(@RequestBody CreateNoteRequest request, Principal principal) {
        return noteService.createNote(request.getNote(), principal.getName());
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable("id") long id) {
        noteService.deleteNoteById(id);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable("id") long id, @RequestBody UpdateNoteRequest request, Principal principal) {
        return noteService.updateNote(id, request.getText(), principal.getName());
    }
}

