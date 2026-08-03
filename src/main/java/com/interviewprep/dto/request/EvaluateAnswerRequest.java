package com.interviewprep.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EvaluateAnswerRequest {

    @NotBlank
    private String sessionId;

    @NotBlank
    private String questionId;

    @NotBlank
    @Size(min = 5, message = "Question text must be at least 5 characters")
    private String questionText;

    @NotBlank
    @Size(min = 2, message = "Answer must be at least 2 characters")
    private String userAnswer;

    @NotBlank
    private String jobRole;

    @NotBlank
    private String experience;

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
}