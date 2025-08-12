package com.notesapp.notes.service;

import com.notesapp.notes.Repository.UserRepo;
import com.notesapp.notes.exception.EmailAlreadyExistsException;
import com.notesapp.notes.exception.UserNotFoundException;
import com.notesapp.notes.model.AuthRequest;
import com.notesapp.notes.model.User;
import com.notesapp.notes.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String registerUser(User user) {
        log.info("Attempting to register user with email: {}", user.getEmail());

        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            log.error("Registration failed: Email {} already exists", user.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        log.info("User {} registered successfully", user.getEmail());
        return "User registered successfully";
    }

    public String loginUser(AuthRequest request) {
        log.info("Attempting to log in user: {}", request.getEmail());

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Login failed: User {} not found", request.getEmail());
                    return new UserNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Login failed: Invalid credentials for {}", request.getEmail());
            throw new RuntimeException("Invalid credentials");
        }

        log.info("User {} logged in successfully", request.getEmail());
        return jwtService.generateToken(user);
    }
}
