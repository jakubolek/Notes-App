package com.jakubolek.notesapp.service;

import com.jakubolek.notesapp.exception.impl.MissingPermissionException;
import com.jakubolek.notesapp.exception.impl.NoteDoesNotExist;
import com.jakubolek.notesapp.model.*;
import com.jakubolek.notesapp.repository.NoteRepository;
import com.jakubolek.notesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Override
    public Note createNote(String content, String username) {
        User user = userRepository.findByUsername(username);
        Note note = NoteFactory.create(content, user);

        return noteRepository.save(note);
    }

    @Override
    public List<NoteResponse> getNotes(String username) {
        User user = userRepository.findByUsername(username);
        List<Note> notes = noteRepository.findByUsername(user);
        notes.sort(new SortNoteByUpdatedTime());
        return notes.stream().map(NoteResponse::new).collect(Collectors.toList());
    }

    @Override
    public void deleteNoteById(long id) {
        if (noteRepository.findById(id).isPresent()) {
            noteRepository.deleteById(id);
        } else {
            throw new NoteDoesNotExist(id);
        }
    }

    @Override
    public Note updateNote(long noteId, String content, String username) {
        User user = userRepository.findByUsername(username);
        Optional<Note> note = noteRepository.findById(noteId);

        if (note.isEmpty()) {
            throw new NoteDoesNotExist(noteId);
        }

        if (note.get().getUsername().equals(user)) {
            note.get().update(content, user);
            return note.get();
        } else {
            throw new MissingPermissionException();
        }
    }
}
