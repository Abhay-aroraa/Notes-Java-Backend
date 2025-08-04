package com.notesapp.notes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")  // this maps to the "users" collection in MongoDB
public class User {

    @Id
    private String id;
    @Indexed(unique = true)

    private String email;
    @NonNull
    private String password;
    private String role;
}
