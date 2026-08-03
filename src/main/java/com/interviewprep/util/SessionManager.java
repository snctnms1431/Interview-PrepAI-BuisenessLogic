package com.interviewprep.util;

import com.interviewprep.dto.response.SessionSummaryResponse;
import com.interviewprep.model.Question;
import com.interviewprep.model.Session;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    // Make these STATIC so they survive Devtools restarts
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Integer>> questionScores = new ConcurrentHashMap<>();

    public void createSession(Session session) {
        System.out.println("✅ Creating session: " + session.getId());
        sessions.put(session.getId(), session);
        questionScores.put(session.getId(), new HashMap<>());
        System.out.println("📊 Total sessions: " + sessions.size());
        System.out.println("📊 All session IDs: " + sessions.keySet());
    }

    public Session getSession(String sessionId) {
        System.out.println("🔍 Looking for session: " + sessionId);
        System.out.println("📊 Available sessions: " + sessions.keySet());
        Session session = sessions.get(sessionId);
        if (session == null) {
            System.out.println("❌ Session NOT found: " + sessionId);
        } else {
            System.out.println("✅ Session FOUND: " + sessionId);
        }
        return session;
    }

    public boolean isSessionComplete(String sessionId) {
        Session session = sessions.get(sessionId);
        if (session == null) {
            return true;
        }
        return session.getCurrentQuestionNumber() >= session.getQuestionCount();
    }

    public void addQuestion(String sessionId, Question question) {
        System.out.println("📝 Adding question to session: " + sessionId);
        Session session = sessions.get(sessionId);
        if (session != null) {
            session.getQuestions().add(question);
            session.setCurrentQuestionNumber(session.getCurrentQuestionNumber() + 1);
            System.out.println("✅ Question " + question.getQuestionNumber() + " added");
        } else {
            System.out.println("❌ Session NOT found for adding question: " + sessionId);
        }
    }

    public void addAnswer(String sessionId, String questionId, String answer, int score) {
        System.out.println("📝 Adding answer for session: " + sessionId);
        Session session = sessions.get(sessionId);
        if (session != null) {
            session.getAnswers().put(questionId, answer);
            Map<String, Integer> scores = questionScores.get(sessionId);
            if (scores != null) {
                scores.put(questionId, score);
            }
            System.out.println("✅ Answer added for question: " + questionId);
        } else {
            System.out.println("❌ Session NOT found for adding answer: " + sessionId);
        }
    }

    public SessionSummaryResponse generateSummary(String sessionId) {
        Session session = sessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }

        Map<String, Integer> scores = questionScores.get(sessionId);
        double averageScore = scores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        List<String> strengths = getTopTopics(scores, true);
        List<String> topicsToImprove = getTopTopics(scores, false);
        List<String> suggestions = generateSuggestions(topicsToImprove);

        String confidence = averageScore >= 7.0 ? "HIGH" :
                averageScore >= 5.0 ? "MEDIUM" : "LOW";

        SessionSummaryResponse response = new SessionSummaryResponse();
        response.setSessionId(sessionId);
        response.setTotalQuestions(session.getQuestions().size());
        response.setAverageScore(Math.round(averageScore * 10.0) / 10.0);
        response.setStrengths(strengths);
        response.setTopicsToImprove(topicsToImprove);
        response.setSuggestions(suggestions);
        response.setQuestionScores(scores);
        response.setConfidence(confidence);

        return response;
    }

    private List<String> getTopTopics(Map<String, Integer> scores, boolean highest) {
        return scores.entrySet().stream()
                .sorted((a, b) -> highest ?
                        b.getValue().compareTo(a.getValue()) :
                        a.getValue().compareTo(b.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<String> generateSuggestions(List<String> topicsToImprove) {
        List<String> suggestions = new ArrayList<>();
        for (String topic : topicsToImprove) {
            suggestions.add("Review " + topic + " concepts thoroughly");
            suggestions.add("Practice more " + topic + " questions");
        }
        return suggestions;
    }
}