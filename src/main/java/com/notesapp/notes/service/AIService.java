package com.notesapp.notes.service;

import com.notesapp.notes.exception.AIServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://openrouter.ai/api/v1/chat/completions";

    public String getAIResponse(String userMessage, String origin) {
        logger.info("Sending AI request for user message: {}", userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String referer = "https://notes-react-frontend.vercel.app";
        headers.add("HTTP-Referer", referer);
        headers.add("X-Title", "Notes App");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/mistral-7b-instruct");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", userMessage)));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String aiResponse = message.get("content").toString();
                    logger.info("Received AI response successfully.");
                    return aiResponse;
                } else {
                    logger.warn("No choices found in AI response.");
                    throw new AIServiceException("No choices found in AI response.");
                }
            } else {
                logger.warn("AI response status not OK: {}", response.getStatusCode());
                throw new AIServiceException("AI service returned status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException httpError) {
            logger.error("HTTP error while contacting AI: {}", httpError.getStatusCode(), httpError);
            throw new AIServiceException("Failed to contact AI service due to client error: " + httpError.getStatusCode(), httpError);
        } catch (Exception e) {
            logger.error("Unexpected error while contacting AI", e);
            throw new AIServiceException("Unexpected error occurred while contacting AI service.", e);
        }
    }
}
