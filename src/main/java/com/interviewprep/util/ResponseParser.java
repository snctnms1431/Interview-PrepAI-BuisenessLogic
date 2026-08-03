package com.interviewprep.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.interviewprep.dto.response.FeedbackResponse;
import com.interviewprep.exception.GeminiApiException;
import com.interviewprep.model.Question;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseParser {

    private final ObjectMapper objectMapper;

    public ResponseParser() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Question parseQuestion(String rawResponse) {
        try {
            String cleaned = cleanResponse(rawResponse);
            JsonNode root = objectMapper.readTree(cleaned);

            Question question = new Question();
            question.setText(root.get("question").asText());
            question.setCategory(root.has("category") ? root.get("category").asText() : "General");
            question.setDifficulty(root.has("difficulty") ? root.get("difficulty").asText() : "MEDIUM");

            return question;

        } catch (Exception e) {
            System.out.println("Error parsing question: " + e.getMessage());
            throw new GeminiApiException("Failed to parse question: " + e.getMessage());
        }
    }

    public FeedbackResponse parseFeedback(String rawResponse) {
        try {
            // First, try to extract JSON from the response
            String cleaned = cleanResponse(rawResponse);

            // If the response contains markdown code blocks, extract JSON
            if (cleaned.contains("```")) {
                cleaned = extractJsonFromMarkdown(cleaned);
            }

            // Parse JSON
            JsonNode root = objectMapper.readTree(cleaned);

            FeedbackResponse feedback = new FeedbackResponse();
            feedback.setScore(root.get("score").asInt());
            feedback.setScoreMax(10);
            feedback.setStrengths(getStringList(root, "strengths"));
            feedback.setWeaknesses(getStringList(root, "weaknesses"));
            feedback.setTopicsToRevise(getStringList(root, "topicsToRevise"));
            feedback.setModelAnswer(root.has("modelAnswer") ? root.get("modelAnswer").asText() : "");
            feedback.setFollowUpQuestions(getStringList(root, "followUpQuestions"));
            feedback.setOverallAssessment(root.has("overallAssessment") ?
                    root.get("overallAssessment").asText() : "");

            return feedback;

        } catch (Exception e) {
            System.out.println("Error parsing feedback: " + e.getMessage());
            throw new GeminiApiException("Failed to parse feedback: " + e.getMessage());
        }
    }

    private String cleanResponse(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "{}";
        }

        // Remove markdown code blocks
        String cleaned = raw.replaceAll("```json\\s*", "");
        cleaned = cleaned.replaceAll("```\\s*", "");

        // Remove trailing commas (common AI mistake)
        cleaned = cleaned.replaceAll(",\\s*}", "}");
        cleaned = cleaned.replaceAll(",\\s*]", "]");

        // Remove any whitespace at start/end
        cleaned = cleaned.trim();

        // If response starts with text before JSON, try to extract JSON
        if (!cleaned.startsWith("{") && !cleaned.startsWith("[")) {
            int jsonStart = cleaned.indexOf("{");
            int jsonEnd = cleaned.lastIndexOf("}");
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                cleaned = cleaned.substring(jsonStart, jsonEnd + 1);
            }
        }

        return cleaned;
    }

    private String extractJsonFromMarkdown(String raw) {
        // Extract content between ```json and ```
        int start = raw.indexOf("```json");
        if (start == -1) {
            start = raw.indexOf("```");
        }
        if (start != -1) {
            int end = raw.indexOf("```", start + 3);
            if (end != -1) {
                String extracted = raw.substring(start + (raw.indexOf("json", start) != -1 ? 7 : 3), end);
                return extracted.trim();
            }
        }
        return raw;
    }

    private List<String> getStringList(JsonNode node, String field) {
        List<String> list = new ArrayList<>();
        JsonNode fieldNode = node.get(field);
        if (fieldNode != null && fieldNode.isArray()) {
            for (JsonNode item : fieldNode) {
                if (!item.isNull() && !item.asText().trim().isEmpty()) {
                    list.add(item.asText().trim());
                }
            }
        }
        return list;
    }
}