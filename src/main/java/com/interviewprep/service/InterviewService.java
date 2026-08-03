package com.interviewprep.service;

import com.interviewprep.dto.request.EvaluateAnswerRequest;
import com.interviewprep.dto.request.NextQuestionRequest;
import com.interviewprep.dto.request.StartInterviewRequest;
import com.interviewprep.dto.response.FeedbackResponse;
import com.interviewprep.dto.response.QuestionResponse;
import com.interviewprep.dto.response.SessionSummaryResponse;
import com.interviewprep.exception.InvalidRequestException;
import com.interviewprep.model.Question;
import com.interviewprep.model.Session;
import com.interviewprep.util.PromptBuilder;
import com.interviewprep.util.ResponseParser;
import com.interviewprep.util.SessionManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InterviewService {

    private final GroqService groqService;
    private final PromptBuilder promptBuilder;
    private final ResponseParser responseParser;
    private final SessionManager sessionManager;

    public InterviewService(GroqService groqService, PromptBuilder promptBuilder,
                            ResponseParser responseParser, SessionManager sessionManager) {
        this.groqService = groqService;
        this.promptBuilder = promptBuilder;
        this.responseParser = responseParser;
        this.sessionManager = sessionManager;
    }

    public QuestionResponse startInterview(StartInterviewRequest request) {
        System.out.println("Starting interview for role: " + request.getJobRole() +
                ", experience: " + request.getExperience() +
                ", difficulty: " + request.getDifficulty());

        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        session.setJobRole(request.getJobRole());
        session.setExperience(request.getExperience());
        session.setDifficulty(request.getDifficulty());
        session.setQuestionCount(request.getQuestionCount());
        session.setCurrentQuestionNumber(0);

        sessionManager.createSession(session);

        String prompt = promptBuilder.buildQuestionPrompt(
                request.getJobRole(),
                request.getExperience(),
                request.getDifficulty()
        );

        String groqResponse = groqService.generateContent(prompt);
        Question question = responseParser.parseQuestion(groqResponse);
        question.setId(UUID.randomUUID().toString());
        question.setQuestionNumber(1);

        sessionManager.addQuestion(session.getId(), question);

        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setQuestion(question.getText());
        response.setCategory(question.getCategory());
        response.setDifficulty(question.getDifficulty());
        response.setQuestionNumber(1);
        response.setTotalQuestions(request.getQuestionCount());
        response.setLast(request.getQuestionCount() == 1);

        return response;
    }

    public FeedbackResponse evaluateAnswer(EvaluateAnswerRequest request) {
        System.out.println("Evaluating answer for question: " + request.getQuestionId());

        String prompt = promptBuilder.buildEvaluationPrompt(
                request.getQuestionText(),
                request.getUserAnswer(),
                request.getJobRole(),
                request.getExperience()
        );

        String groqResponse = groqService.generateContent(prompt);
        FeedbackResponse feedback = responseParser.parseFeedback(groqResponse);
        feedback.setQuestionId(request.getQuestionId());

        sessionManager.addAnswer(
                request.getSessionId(),
                request.getQuestionId(),
                request.getUserAnswer(),
                feedback.getScore()
        );

        return feedback;
    }

    public QuestionResponse getNextQuestion(NextQuestionRequest request) {
        System.out.println("🔄 Getting next question for session: " + request.getSessionId());

        Session session = sessionManager.getSession(request.getSessionId());
        if (session == null) {
            System.out.println("❌ Session not found in getNextQuestion: " + request.getSessionId());
            throw new InvalidRequestException("Session not found: " + request.getSessionId());
        }

        if (session.getCurrentQuestionNumber() >= session.getQuestionCount()) {
            throw new InvalidRequestException("Session is complete");
        }
        String prompt = promptBuilder.buildQuestionPrompt(
                session.getJobRole(),
                session.getExperience(),
                session.getDifficulty()
        );

        String groqResponse = groqService.generateContent(prompt);
        Question question = responseParser.parseQuestion(groqResponse);
        question.setId(UUID.randomUUID().toString());
        question.setQuestionNumber(session.getCurrentQuestionNumber() + 1);

        sessionManager.addQuestion(session.getId(), question);

        boolean isLast = question.getQuestionNumber() >= session.getQuestionCount();

        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setQuestion(question.getText());
        response.setCategory(question.getCategory());
        response.setDifficulty(question.getDifficulty());
        response.setQuestionNumber(question.getQuestionNumber());
        response.setTotalQuestions(session.getQuestionCount());
        response.setLast(isLast);

        return response;
    }

    public SessionSummaryResponse endSession(String sessionId) {
        System.out.println("Ending session: " + sessionId);
        return sessionManager.generateSummary(sessionId);
    }

    public Map<String, Object> getSessionStatus(String sessionId) {
        System.out.println("Getting status for session: " + sessionId);

        Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            throw new InvalidRequestException("Session not found: " + sessionId);
        }

        Map<String, Object> status = new HashMap<>();
        status.put("sessionId", session.getId());
        status.put("jobRole", session.getJobRole());
        status.put("experience", session.getExperience());
        status.put("difficulty", session.getDifficulty());
        status.put("totalQuestions", session.getQuestionCount());
        status.put("answeredQuestions", session.getAnswers().size());
        status.put("remainingQuestions", session.getQuestionCount() - session.getAnswers().size());
        status.put("isComplete", session.getCurrentQuestionNumber() >= session.getQuestionCount());
        status.put("startTime", session.getStartTime());

        return status;
    }
}