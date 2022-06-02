package com.jakubolek.notesapp.unitTests;

import com.jakubolek.notesapp.model.Note;
import com.jakubolek.notesapp.repository.NoteRepository;
import com.jakubolek.notesapp.service.NoteService;
import com.jakubolek.notesapp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.jakubolek.notesapp.utils.NoteDefaults.defaultContent;
import static com.jakubolek.notesapp.utils.UserDefaults.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteTests {

    private final String updatedContent = "new content";

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @BeforeAll
    void setUp() {
        createNewUser();
    }

    @Test
    void shouldCreateNewNote() {
        Note note = noteService.createNote(defaultContent, defaultUsername);

        Optional<Note> createdNote = noteRepository.findById(note.getId());

        assertThat(createdNote.get().getContent()).isEqualTo(defaultContent);
    }

    @Test
    void shouldUpdateNote() {
        Note note = noteService.createNote(defaultContent, defaultUsername);

        noteService.updateNote(note.getId(), updatedContent, defaultUsername);

        Optional<Note> updatedNote = noteRepository.findById(note.getId());
        assertThat(updatedNote.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    void shouldNotUpdateNoteWhenUsernameIsNotCorrect() {
        Note note = noteService.createNote(defaultContent, defaultUsername);

        Exception exception = assertThrows(RuntimeException.class, () ->
                noteService.updateNote(note.getId(), updatedContent, noteExistUser));

        assertThat(exception.getMessage()).isEqualTo(missingPermissionMessage);
        assertThat(note.getContent()).isNotEqualTo(updatedContent);
    }

    @Test
    void shouldDeleteNote() {
        Note note = noteService.createNote(defaultContent, defaultUsername);

        noteService.deleteNoteById(note.getId());
        Optional<Note> deletedNote = noteRepository.findById(note.getId());
        assertThat(deletedNote).isEmpty();
    }

    private void createNewUser() {
        userService.saveUser(defaultUsername, defaultEmail, defaultPassword);
    }
}