package com.notesapp.notes.controller;

import com.notesapp.notes.Repository.UserRepo;
import com.notesapp.notes.exception.NoteNotFoundException;
import com.notesapp.notes.exception.UserNotFoundException;
import com.notesapp.notes.model.NoteRequest;
import com.notesapp.notes.model.User;
import com.notesapp.notes.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/note")
@CrossOrigin(origins = "*")
@Slf4j
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public List<NoteRequest> getAllNotes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("API Request: Get all notes for user {}", email);
        return noteService.getAllNotes();
    }

    @PostMapping
    public NoteRequest createNote(@RequestBody NoteRequest note) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("API Request: User {} creating a new note with title: {}", email, note.getTitle());

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

        note.setUserId(user.getId());
        return noteService.saveNote(note);
    }

    @DeleteMapping("/id/{noteId}")
    public NoteRequest deleteNoteById(@PathVariable String noteId) {
        log.info("API Request: Delete note {}", noteId);
        return noteService.deleteNoteById(noteId); // throws NoteNotFoundException if not found
    }

    @PutMapping("id/{noteId}")
    public ResponseEntity<NoteRequest> updateNote(@PathVariable String noteId, @RequestBody NoteRequest note) {
        log.info("API Request: Update note {}", noteId);
        return noteService.updateNote(noteId, note)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));
    }

    @PutMapping("pin/{noteId}")
    public ResponseEntity<NoteRequest> pinNote(@PathVariable String noteId) {
        log.info("API Request: Toggle pin for note {}", noteId);
        NoteRequest noteRequest = noteService.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));

        noteRequest.setPinned(!noteRequest.isPinned());
        return ResponseEntity.ok(noteService.saveNote(noteRequest));
    }

    @PutMapping("archieve/{noteId}")
    public ResponseEntity<NoteRequest> archiveNote(@PathVariable String noteId) {
        log.info("API Request: Toggle archive for note {}", noteId);
        NoteRequest noteRequest = noteService.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));

        noteRequest.setArchieve(!noteRequest.isArchieve());
        return ResponseEntity.ok(noteService.saveNote(noteRequest));
    }

    @PutMapping("trash/{noteId}")
    public ResponseEntity<NoteRequest> trashNote(@PathVariable String noteId) {
        log.info("API Request: Toggle trash for note {}", noteId);
        NoteRequest noteRequest = noteService.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));

        noteRequest.setTrash(!noteRequest.isTrash());
        return ResponseEntity.ok(noteService.saveNote(noteRequest));
    }

    @PutMapping("color/{noteId}")
    public ResponseEntity<NoteRequest> updateColor(@PathVariable String noteId, @RequestBody NoteRequest request) {
        log.info("API Request: Update color for note {} to {}", noteId, request.getColor());
        NoteRequest note = noteService.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with ID: " + noteId));

        note.setColor(request.getColor());
        return ResponseEntity.ok(noteService.saveNote(note));
    }
}
