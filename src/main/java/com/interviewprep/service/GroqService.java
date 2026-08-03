package com.interviewprep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewprep.exception.GeminiApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    @Value("${groq.model:mixtral-8x7b-32768}")
    private String model;

    @Value("${groq.temperature:0.7}")
    private double temperature;

    @Value("${groq.max-tokens:2048}")
    private int maxTokens;

    public GroqService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public String generateContent(String prompt) {
        try {
            System.out.println("\n=== Groq API Debug ===");
            System.out.println("Model: " + model);
            System.out.println("API Key: " + (apiKey != null && !apiKey.isEmpty() ? "✅ SET" : "❌ NOT SET"));
            System.out.println("Temperature: " + temperature);
            System.out.println("Max Tokens: " + maxTokens);
            System.out.println("=========================\n");

            if (apiKey == null || apiKey.isEmpty()) {
                throw new GeminiApiException("Groq API Key is not configured");
            }

            // Build request body for Groq
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("temperature", temperature);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("messages", List.of(
                    Map.of("role", "user", "content", prompt)
            ));

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            System.out.println("Request Body: " + jsonBody);

            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Groq API error: Status=" + response.getStatusCode());
                System.out.println("Response: " + response.getBody());
                throw new GeminiApiException("Groq API returned error: " + response.getStatusCode());
            }

            String generatedText = extractContentFromResponse(response.getBody());
            System.out.println("✅ Successfully generated content from Groq API");
            return generatedText;

        } catch (GeminiApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Error calling Groq API: " + e.getMessage());
            e.printStackTrace();
            throw new GeminiApiException("Failed to call Groq API: " + e.getMessage());
        }
    }

    private String extractContentFromResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode choices = root.get("choices");

            if (choices == null || !choices.isArray() || choices.isEmpty()) {
                throw new GeminiApiException("No choices in Groq response");
            }

            JsonNode message = choices.get(0).get("message");
            if (message == null) {
                throw new GeminiApiException("No message in Groq response");
            }

            JsonNode content = message.get("content");
            if (content == null) {
                throw new GeminiApiException("No content in Groq response");
            }

            String text = content.asText();

            // Clean the response - remove markdown and extra whitespace
            text = text.replaceAll("```json\\s*", "");
            text = text.replaceAll("```\\s*", "");
            text = text.trim();

            // Extract JSON if there's extra text
            if (!text.startsWith("{") && text.contains("{")) {
                int start = text.indexOf("{");
                int end = text.lastIndexOf("}") + 1;
                if (start >= 0 && end > start) {
                    text = text.substring(start, end);
                }
            }

            return text;

        } catch (Exception e) {
            System.out.println("Failed to extract content: " + e.getMessage());
            throw new GeminiApiException("Failed to parse Groq response: " + e.getMessage());
        }
    }
}