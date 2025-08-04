package com.notesapp.notes.controller;

import com.notesapp.notes.model.AuthRequest;
import com.notesapp.notes.model.User;
import com.notesapp.notes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

@PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        String result = userService.registerUser(user);
        return ResponseEntity.ok(result);
    }
@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request){
    String token = userService.loginUser(request);
    return ResponseEntity.ok(token);
}
}
