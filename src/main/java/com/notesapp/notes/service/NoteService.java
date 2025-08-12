package com.notesapp.notes.service;

import com.notesapp.notes.Repository.NotesRepo;
import com.notesapp.notes.Repository.UserRepo;
import com.notesapp.notes.exception.NoteNotFoundException;
import com.notesapp.notes.exception.UserNotFoundException;
import com.notesapp.notes.model.NoteRequest;
import com.notesapp.notes.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class NoteService {

    private final NotesRepo notesRepo;
    private final UserRepo userRepo;

    @Autowired
    public NoteService(NotesRepo notesRepo, UserRepo userRepo) {
        this.notesRepo = notesRepo;
        this.userRepo = userRepo;
    }

    public List<NoteRequest> getAllNotes() {
        String email = getCurrentUserEmail();
        log.info("Fetching all notes for user: {}", email);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return notesRepo.findAllByUserId(user.getId());
    }

    public NoteRequest saveNote(NoteRequest note) {
        log.info("Saving note for userId: {}", note.getUserId());
        return notesRepo.save(note);
    }

    public Optional<NoteRequest> findById(String noteId) {
        log.info("Finding note by ID: {}", noteId);
        return notesRepo.findById(noteId);
    }

    public NoteRequest deleteNoteById(String noteId) {
        log.info("Deleting note by ID: {}", noteId);
        NoteRequest note = notesRepo.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));
        notesRepo.deleteById(noteId);
        return note;
    }

    public Optional<NoteRequest> updateNote(String noteId, NoteRequest note) {
        log.info("Updating note with ID: {}", noteId);
        return notesRepo.findById(noteId).map(existingNote -> {
            existingNote.setTitle(note.getTitle());
            existingNote.setContent(note.getContent());
            return notesRepo.save(existingNote);
        }).or(() -> {
            throw new NoteNotFoundException("Note not found with ID: " + noteId);
        });
    }

    public Optional<NoteRequest> getNoteForCurrentUser(String noteId) {
        String email = getCurrentUserEmail();
        log.info("Fetching note with ID: {} for user: {}", noteId, email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

        return notesRepo.findById(noteId)
                .filter(note -> note.getUserId().equals(user.getId()))
                .or(() -> {
                    throw new NoteNotFoundException("Note not found or does not belong to user");
                });
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
