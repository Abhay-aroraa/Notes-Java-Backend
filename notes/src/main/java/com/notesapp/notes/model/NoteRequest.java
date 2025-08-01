package com.notesapp.notes.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
@Data
public class NoteRequest {

    @Id
    private String id;

    public boolean pinned = false;
    public boolean archieve = false;
    public boolean trash = false;
    private String color;
    private String userId;
    private String title;
    private String content;
}
