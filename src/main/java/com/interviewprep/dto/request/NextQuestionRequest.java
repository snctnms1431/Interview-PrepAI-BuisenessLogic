package com.interviewprep.dto.request;

import jakarta.validation.constraints.NotBlank;

public class NextQuestionRequest {

    @NotBlank
    private String sessionId;

    @NotBlank
    private String previousQuestionId;

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getPreviousQuestionId() { return previousQuestionId; }
    public void setPreviousQuestionId(String previousQuestionId) { this.previousQuestionId = previousQuestionId; }
}