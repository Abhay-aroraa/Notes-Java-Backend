package com.notesapp.notes;

import com.notesapp.notes.Repository.NotesRepo;
import com.notesapp.notes.model.NoteRequest;
import com.notesapp.notes.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NotesRepo notesRepo;

    @InjectMocks
    private NoteService noteService;

    private NoteRequest testNote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testNote = new NoteRequest();
        testNote.setId("1");
        testNote.setTitle("Test Title");
        testNote.setContent("Test Content");
        testNote.setPinned(false);
    }

    @Test
    void testAddNote() {
        when(notesRepo.save(testNote)).thenReturn(testNote);

        NoteRequest result = noteService.saveNote(testNote);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(notesRepo, times(1)).save(testNote);
    }

    @Test
    void testGetNoteById() {
        when(notesRepo.findById("1")).thenReturn(Optional.of(testNote));

        Optional<NoteRequest> result = noteService.findById("1");

        assertTrue(result.isPresent());
        assertEquals("Test Content", result.get().getContent());
        verify(notesRepo, times(1)).findById("1");
    }

    @Test
    void testUpdateNote() {
        when(notesRepo.findById("1")).thenReturn(Optional.of(testNote));
        when(notesRepo.save(testNote)).thenReturn(testNote);

        testNote.setTitle("Updated Title");
        Optional<NoteRequest> updatedNote = noteService.updateNote("1", testNote);

        assertEquals("Updated Title", updatedNote.get());
        verify(notesRepo, times(1)).save(testNote);
    }

    @Test
    void testDeleteNote() {
        when(notesRepo.existsById("1")).thenReturn(true);

        NoteRequest deleted = noteService.deleteNoteById("1");

        assertTrue((BooleanSupplier) deleted);
        verify(notesRepo, times(1)).deleteById("1");
    }

    @Test
    void testGetAllNotes() {
        when(notesRepo.findAll()).thenReturn(Collections.singletonList(testNote));

        List<NoteRequest> notes = noteService.getAllNotes();

        assertEquals(1, notes.size());
        verify(notesRepo, times(1)).findAll();
    }
}
