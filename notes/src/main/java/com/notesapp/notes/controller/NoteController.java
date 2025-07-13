package com.notesapp.notes.controller;


import com.notesapp.notes.model.NoteRequest;
import com.notesapp.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/note")
@CrossOrigin(origins = "*")
public class NoteController {

@Autowired
private NoteService noteService;

@GetMapping
public List<NoteRequest> getAllNotes(){
return noteService.getAllNotes();
}

@PostMapping
public NoteRequest createNote(@RequestBody NoteRequest note) {
        return noteService.saveNote(note);
}
 @DeleteMapping("/id/{noteId}")
    public NoteRequest deleteNoteById(@PathVariable String noteId) {
        return noteService.deleteNoteById(noteId);
    }@PutMapping("id/{noteId}")
    public ResponseEntity<NoteRequest> updatesNote(@PathVariable String noteId, @RequestBody NoteRequest note) {
        return noteService.updateNote(noteId, note)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());            // 404 Not Found
    }
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

    @PutMapping("archieve/{noteId}")
    public ResponseEntity<NoteRequest> archieveNote(@PathVariable String noteId){
    Optional<NoteRequest> optionalArchieve = noteService.findById(noteId);

    if(optionalArchieve.isPresent()){
        NoteRequest noteRequest = optionalArchieve.get();
        noteRequest.setArchieve(!noteRequest.isArchieve());
        return ResponseEntity.ok(noteService.saveNote(noteRequest));
    }else {
        return ResponseEntity.notFound().build();
    }

    }

    @PutMapping("trash/{noteId}")
    public ResponseEntity<NoteRequest> trashNote(@PathVariable String noteId){
        Optional<NoteRequest> optionalTrash = noteService.findById(noteId);

        if(optionalTrash.isPresent()){
            NoteRequest noteRequest = optionalTrash.get();
            noteRequest.setTrash(!noteRequest.isTrash());

            return ResponseEntity.ok(noteService.saveNote(noteRequest));
        }else {
            return ResponseEntity.notFound().build();
        }

    }


}
