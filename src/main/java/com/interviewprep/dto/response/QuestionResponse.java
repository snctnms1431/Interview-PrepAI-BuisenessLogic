package com.interviewprep.dto.response;

public class QuestionResponse {
    private String id;  // This is the SESSION ID
    private String question;
    private String category;
    private String difficulty;
    private int questionNumber;
    private int totalQuestions;
    private boolean isLast;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getQuestionNumber() { return questionNumber; }
    public void setQuestionNumber(int questionNumber) { this.questionNumber = questionNumber; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public boolean isLast() { return isLast; }
    public void setLast(boolean last) { isLast = last; }
}