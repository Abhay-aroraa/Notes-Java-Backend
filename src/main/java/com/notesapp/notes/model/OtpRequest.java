package com.notesapp.notes.model;


import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private String otp;
    private String newPassword;
    private long expiryTime;
}
