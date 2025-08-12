package com.notesapp.notes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IsActiveController {

    @GetMapping("/active")
    public String isActive() {
        return "Yes";
    }
}
