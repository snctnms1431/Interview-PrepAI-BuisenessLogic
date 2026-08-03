package com.interviewprep.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StartInterviewRequest {

    @NotBlank(message = "Job role is required")
    @Size(min = 2, max = 50, message = "Job role must be between 2 and 50 characters")
    private String jobRole;

    @NotBlank(message = "Experience level is required")
    private String experience;

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    @Min(value = 1, message = "Question count must be at least 1")
    private int questionCount = 5;

    // Getters and Setters
    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getQuestionCount() { return questionCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }
}