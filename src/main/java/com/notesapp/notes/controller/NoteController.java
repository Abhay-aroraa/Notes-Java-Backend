package com.notesapp.notes.controller;

import com.notesapp.notes.Repository.UserRepo;
import com.notesapp.notes.model.NoteRequest;
import com.notesapp.notes.model.User;
import com.notesapp.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/note")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserRepo userRepo;

    // ✅ Get all notes of logged-in user
    @GetMapping
    public List<NoteRequest> getAllNotes() {
        return noteService.getAllNotes(); // this handles email → userId → fetch notes
    }

    // ✅ Create a new note and assign userId
    @PostMapping
    public NoteRequest createNote(@RequestBody NoteRequest note) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        note.setUserId(user.getId());
        return noteService.saveNote(note);
    }

    // ✅ Delete note
    @DeleteMapping("/id/{noteId}")
    public NoteRequest deleteNoteById(@PathVariable String noteId) {
        return noteService.deleteNoteById(noteId);
    }

    // ✅ Update note content
    @PutMapping("id/{noteId}")
    public ResponseEntity<NoteRequest> updatesNote(@PathVariable String noteId, @RequestBody NoteRequest note) {
        return noteService.updateNote(noteId, note)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Pin/unpin note
    @PutMapping("pin/{noteId}")
    public ResponseEntity<NoteRequest> pinNotes(@PathVariable String noteId) {
        Optional<NoteRequest> noteRequestOptional = noteService.findById(noteId);
        if (noteRequestOptional.isPresent()) {
            NoteRequest noteRequest = noteRequestOptional.get();
            noteRequest.setPinned(!noteRequest.isPinned());
            return ResponseEntity.ok(noteService.saveNote(noteRequest));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Archive/unarchive note
    @PutMapping("archieve/{noteId}")
    public ResponseEntity<NoteRequest> archieveNote(@PathVariable String noteId) {
        Optional<NoteRequest> optionalArchieve = noteService.findById(noteId);
        if (optionalArchieve.isPresent()) {
            NoteRequest noteRequest = optionalArchieve.get();
            noteRequest.setArchieve(!noteRequest.isArchieve());
            return ResponseEntity.ok(noteService.saveNote(noteRequest));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Trash/restore note
    @PutMapping("trash/{noteId}")
    public ResponseEntity<NoteRequest> trashNote(@PathVariable String noteId) {
        Optional<NoteRequest> optionalTrash = noteService.findById(noteId);
        if (optionalTrash.isPresent()) {
            NoteRequest noteRequest = optionalTrash.get();
            noteRequest.setTrash(!noteRequest.isTrash());
            return ResponseEntity.ok(noteService.saveNote(noteRequest));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Update note color
    @PutMapping("color/{noteId}")
    public ResponseEntity<?> updateColor(@PathVariable String noteId, @RequestBody NoteRequest request) {
        NoteRequest note = noteService.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setColor(request.getColor());
        return ResponseEntity.ok(noteService.saveNote(note));
    }
}
