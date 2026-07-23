package com.notesapp.notes.service;

import com.notesapp.notes.exception.AIServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public String getAIResponse(String userMessage, String origin) {

        logger.info("Sending Gemini request: {}", userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", userMessage)
                                )
                        )
                )
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(API_URL, request, Map.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new AIServiceException("Gemini returned an empty response.");
            }

            Map<String, Object> body = response.getBody();

            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) body.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                throw new AIServiceException("No candidates returned by Gemini.");
            }

            Map<String, Object> content =
                    (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");

            if (parts == null || parts.isEmpty()) {
                throw new AIServiceException("No text returned by Gemini.");
            }

            String aiResponse = parts.get(0).get("text").toString();

            logger.info("Gemini response received successfully.");

            return aiResponse;

        } catch (HttpClientErrorException e) {

            logger.error("Gemini API Error: {}", e.getResponseBodyAsString());

            throw new AIServiceException(
                    "Gemini API Error: " + e.getResponseBodyAsString(),
                    e
            );

        } catch (Exception e) {

            logger.error("Unexpected error while contacting Gemini", e);

            throw new AIServiceException(
                    "Unexpected error while contacting Gemini.",
                    e
            );
        }
    }
}
