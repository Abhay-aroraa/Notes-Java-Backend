package com.notesapp.notes.service;

import com.notesapp.notes.Repository.NotesRepo;
import com.notesapp.notes.model.NoteRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NotesRepo notesRepo;

    public NoteService(NotesRepo notesRepo) {
        this.notesRepo = notesRepo;
    }

    public List<NoteRequest> getAllNotes() {
        return notesRepo.findAll();
    }

    public NoteRequest saveNote(NoteRequest note) {
        return notesRepo.save(note);
    }

    public Optional<NoteRequest> findById(String noteId){
        return notesRepo.findById(noteId);
    }
    public NoteRequest deleteNoteById(String noteId) {
        NoteRequest note = notesRepo.findById(noteId).orElse(null);
        if (note != null) {
            notesRepo.deleteById(noteId);
        }
        return note;
    }

    public Optional<NoteRequest> updateNote(String noteId, @RequestBody NoteRequest note){
        return notesRepo.findById(noteId).map(exitingNote -> {
            exitingNote.setTitle(note.getTitle());
            exitingNote.setContent(note.getContent());
            return notesRepo.save(exitingNote);

        });
    }
}
