package com.interviewprep.dto.response;

import java.util.List;

public class FeedbackResponse {
    private String questionId;
    private int score;
    private int scoreMax;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> topicsToRevise;
    private String modelAnswer;
    private List<String> followUpQuestions;
    private String overallAssessment;

    // Getters and Setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getScoreMax() { return scoreMax; }
    public void setScoreMax(int scoreMax) { this.scoreMax = scoreMax; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public List<String> getWeaknesses() { return weaknesses; }
    public void setWeaknesses(List<String> weaknesses) { this.weaknesses = weaknesses; }

    public List<String> getTopicsToRevise() { return topicsToRevise; }
    public void setTopicsToRevise(List<String> topicsToRevise) { this.topicsToRevise = topicsToRevise; }

    public String getModelAnswer() { return modelAnswer; }
    public void setModelAnswer(String modelAnswer) { this.modelAnswer = modelAnswer; }

    public List<String> getFollowUpQuestions() { return followUpQuestions; }
    public void setFollowUpQuestions(List<String> followUpQuestions) { this.followUpQuestions = followUpQuestions; }

    public String getOverallAssessment() { return overallAssessment; }
    public void setOverallAssessment(String overallAssessment) { this.overallAssessment = overallAssessment; }
}