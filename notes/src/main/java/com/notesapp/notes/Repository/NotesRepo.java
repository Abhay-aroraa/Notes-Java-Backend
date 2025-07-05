package com.notesapp.notes.Repository;

import com.notesapp.notes.model.NoteRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotesRepo extends MongoRepository<NoteRequest,String> {


}
