package com.notesapp.notes.controller;

import com.notesapp.notes.Repository.UserRepo;
import com.notesapp.notes.model.User;
import com.notesapp.notes.service.EmailService;
import com.notesapp.notes.service.OtpService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        try {
            logger.info("Request to send OTP for email: {}", email);
            Optional<User> user = userRepo.findByEmail(email);

            if (user.isEmpty()) {
                logger.warn("User not found for email: {}", email);
                return ResponseEntity.badRequest().body("User not found");
            }

            String otp = otpService.generateOtp(email);
            emailService.sendOtpEmail(email, otp);
            logger.info("OTP sent successfully to email: {}", email);
            return ResponseEntity.ok("OTP sent successfully");
        } catch (Exception ex) {
            logger.error("Error sending OTP to email: {}", email, ex);
            return ResponseEntity.internalServerError().body("Failed to send OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword
    ) {
        try {
            logger.info("Password reset request for email: {}", email);

            if (!otpService.verifyOtp(email, otp)) {
                logger.warn("Invalid or expired OTP for email: {}", email);
                return ResponseEntity.badRequest().body("Invalid or expired OTP");
            }

            Optional<User> userOpt = userRepo.findByEmail(email);
            if (userOpt.isEmpty()) {
                logger.warn("User not found for email: {}", email);
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            otpService.removeOtp(email);
            logger.info("Password updated successfully for email: {}", email);

            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception ex) {
            logger.error("Error resetting password for email: {}", email, ex);
            return ResponseEntity.internalServerError().body("Failed to reset password");
        }
    }
}
