package com.interviewprep.controller;

import com.interviewprep.dto.request.EvaluateAnswerRequest;
import com.interviewprep.dto.request.NextQuestionRequest;
import com.interviewprep.dto.request.StartInterviewRequest;
import com.interviewprep.dto.response.ApiResponse;
import com.interviewprep.dto.response.FeedbackResponse;
import com.interviewprep.dto.response.QuestionResponse;
import com.interviewprep.dto.response.SessionSummaryResponse;
import com.interviewprep.service.InterviewService;
import com.interviewprep.util.JobRoles;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<QuestionResponse>> startInterview(
            @Valid @RequestBody StartInterviewRequest request) {
        System.out.println("Starting interview for role: " + request.getJobRole());
        QuestionResponse response = interviewService.startInterview(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<FeedbackResponse>> evaluateAnswer(
            @Valid @RequestBody EvaluateAnswerRequest request) {
        System.out.println("Evaluating answer for question: " + request.getQuestionId());
        FeedbackResponse response = interviewService.evaluateAnswer(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/next")
    public ResponseEntity<ApiResponse<QuestionResponse>> getNextQuestion(
            @Valid @RequestBody NextQuestionRequest request) {
        System.out.println("Getting next question for session: " + request.getSessionId());
        QuestionResponse response = interviewService.getNextQuestion(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/end")
    public ResponseEntity<ApiResponse<SessionSummaryResponse>> endSession(
            @RequestParam String sessionId) {
        System.out.println("Ending session: " + sessionId);
        SessionSummaryResponse response = interviewService.endSession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRoles() {
        System.out.println("Fetching available job roles");
        Map<String, Object> response = new HashMap<>();
        response.put("roles", JobRoles.ALL_ROLES);
        response.put("experienceLevels", JobRoles.EXPERIENCE_LEVELS);
        response.put("difficultyLevels", JobRoles.DIFFICULTY_LEVELS);
        response.put("questionCounts", JobRoles.QUESTION_COUNTS);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/session/{sessionId}/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSessionStatus(
            @PathVariable String sessionId) {
        System.out.println("Getting status for session: " + sessionId);
        Map<String, Object> status = interviewService.getSessionStatus(sessionId);
        return ResponseEntity.ok(ApiResponse.success(status));
    }
}