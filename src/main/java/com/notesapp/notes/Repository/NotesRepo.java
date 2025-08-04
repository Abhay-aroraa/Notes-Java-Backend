package com.notesapp.notes.Repository;

import com.notesapp.notes.model.NoteRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotesRepo extends MongoRepository<NoteRequest,String> {

    List<NoteRequest> findAllByUserId(String userId);

}
