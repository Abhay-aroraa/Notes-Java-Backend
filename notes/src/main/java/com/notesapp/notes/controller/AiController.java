package com.notesapp.notes.controller;


import com.notesapp.notes.service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AIService aiService;

    public AiController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> getAIResponse(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String response = aiService.getAIResponse(prompt);

        Map<String, String> result = new HashMap<>();
        result.put("response", response);

        return ResponseEntity.ok(result);
    }
}
