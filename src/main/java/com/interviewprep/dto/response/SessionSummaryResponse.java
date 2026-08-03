package com.interviewprep.dto.response;

import java.util.List;
import java.util.Map;

public class SessionSummaryResponse {
    private String sessionId;
    private int totalQuestions;
    private double averageScore;
    private List<String> strengths;
    private List<String> topicsToImprove;
    private List<String> suggestions;
    private Map<String, Integer> questionScores;
    private String confidence;

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public List<String> getTopicsToImprove() { return topicsToImprove; }
    public void setTopicsToImprove(List<String> topicsToImprove) { this.topicsToImprove = topicsToImprove; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public Map<String, Integer> getQuestionScores() { return questionScores; }
    public void setQuestionScores(Map<String, Integer> questionScores) { this.questionScores = questionScores; }

    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }
}