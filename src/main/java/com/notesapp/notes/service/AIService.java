package com.notesapp.notes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://openrouter.ai/api/v1/chat/completions";

    public String getAIResponse(String userMessage, String origin) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 🔐 Always use production referer — localhost gets blocked by OpenRouter
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
                    return message.get("content").toString();
                }
            }
            return "No response from AI.";

        } catch (org.springframework.web.client.HttpClientErrorException httpError) {
            System.out.println("========= AI ERROR START =========");
            System.out.println("Status Code: " + httpError.getStatusCode());
            System.out.println("Response Body: " + httpError.getResponseBodyAsString());
            System.out.println("========= AI ERROR END =========");
            return "Something went wrong while contacting AI.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong while contacting AI.";
        }

    }
}
