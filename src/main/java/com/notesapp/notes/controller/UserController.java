package com.notesapp.notes.controller;

import com.notesapp.notes.model.AuthRequest;
import com.notesapp.notes.model.User;
import com.notesapp.notes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        log.info("Received registration request for email: {}", user.getEmail());
        String result = userService.registerUser(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request){
        log.info("Received login request for email: {}", request.getEmail());
        String token = userService.loginUser(request);
        return ResponseEntity.ok(token);
    }
}
