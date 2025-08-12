package com.notesapp.notes.controller;

import com.notesapp.notes.service.AIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    private final AIService aiService;

    public AiController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> getAIResponse(
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "Origin", required = false) String origin
    ) {
        String prompt = request.get("prompt");
        if (prompt == null || prompt.trim().isEmpty()) {
            logger.warn("Received AI request with empty prompt");
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Prompt is required"));
        }

        logger.info("Received AI request with prompt: {}", prompt);

        String response = aiService.getAIResponse(prompt, origin);

        Map<String, String> result = new HashMap<>();
        result.put("response", response);

        return ResponseEntity.ok(result);
    }
}
