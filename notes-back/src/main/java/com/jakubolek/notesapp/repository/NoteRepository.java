package com.jakubolek.notesapp.repository;

import com.jakubolek.notesapp.model.Note;
import com.jakubolek.notesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUsername(User user);
}