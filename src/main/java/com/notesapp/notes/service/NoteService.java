package com.notesapp.notes.service;

import com.notesapp.notes.Repository.NotesRepo;
import com.notesapp.notes.Repository.UserRepo;
import com.notesapp.notes.model.NoteRequest;
import com.notesapp.notes.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NotesRepo notesRepo;
    private final UserRepo userRepo;

    @Autowired
    public NoteService(NotesRepo notesRepo, UserRepo userRepo) {
        this.notesRepo = notesRepo;
        this.userRepo = userRepo;
    }

    // ✅ Get all notes of the logged-in user
    public List<NoteRequest> getAllNotes() {
        String email = getCurrentUserEmail();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return notesRepo.findAllByUserId(user.getId());
    }

    // ✅ Save a note (userId must be already set)
    public NoteRequest saveNote(NoteRequest note) {
        return notesRepo.save(note);
    }

    // ✅ Find note by ID
    public Optional<NoteRequest> findById(String noteId) {
        return notesRepo.findById(noteId);
    }

    // ✅ Delete note by ID
    public NoteRequest deleteNoteById(String noteId) {
        Optional<NoteRequest> note = notesRepo.findById(noteId);
        note.ifPresent(n -> notesRepo.deleteById(noteId));
        return note.orElse(null);
    }

    // ✅ Update title & content of a note
    public Optional<NoteRequest> updateNote(String noteId, NoteRequest note) {
        return notesRepo.findById(noteId).map(existingNote -> {
            existingNote.setTitle(note.getTitle());
            existingNote.setContent(note.getContent());
            return notesRepo.save(existingNote);
        });
    }

    // ✅ Helper to get logged-in user's email
    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // ❓ (Optional) Get one note by its ID + check owner
    public Optional<NoteRequest> getNoteForCurrentUser(String noteId) {
        String email = getCurrentUserEmail();
        Optional<User> user = userRepo.findByEmail(email);

        if (user.isEmpty()) throw new RuntimeException("User not found");

        return notesRepo.findById(noteId)
                .filter(note -> note.getUserId().equals(user.get().getId()));
    }
}
