package com.notesapp.notes.controller;


import com.notesapp.notes.Repository.UserRepo;
import com.notesapp.notes.model.OtpRequest;
import com.notesapp.notes.model.User;
import com.notesapp.notes.service.EmailService;
import com.notesapp.notes.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


@PostMapping("/send-otp")
public ResponseEntity<String> sendOtp(@RequestParam String email){
    Optional<User> user = userRepo.findByEmail(email);
    if(user.isEmpty()) return ResponseEntity.badRequest().body("user Not Found");

    String otp = otpService.generateOtp(email);
    emailService.sendOtpEmail(email,otp);
    return ResponseEntity.ok("OTP sent successfully");

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword
    ){
    if(!otpService.verifyOtp(email, otp)){
         return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }
        User user = userRepo.findByEmail(email).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        otpService.removeOtp(email);
        return ResponseEntity.ok("Password updated successfully");
    }
}
