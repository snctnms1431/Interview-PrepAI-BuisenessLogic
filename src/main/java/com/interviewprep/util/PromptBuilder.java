package com.interviewprep.util;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    // Enhanced question generation with more context
    public String buildQuestionPrompt(String role, String experience, String difficulty) {
        String roleContext = getRoleContext(role);
        String experienceContext = getExperienceContext(experience);
        String difficultyContext = getDifficultyContext(difficulty);

        // Escape any % characters
        String safeRole = role.replace("%", "%%");
        String safeExperience = experience.replace("%", "%%");
        String safeDifficulty = difficulty.replace("%", "%%");
        String safeRoleContext = roleContext.replace("%", "%%");
        String safeExperienceContext = experienceContext.replace("%", "%%");
        String safeDifficultyContext = difficultyContext.replace("%", "%%");

        return String.format("""
        You are a senior technical interviewer at a FAANG company with 15+ years of experience.
        
        ROLE: %s
        EXPERIENCE: %s
        DIFFICULTY: %s
        
        %s
        %s
        %s
        
        Generate 1 unique, high-quality interview question that:
        1. Tests practical knowledge, not just theory
        2. Requires critical thinking
        3. Is relevant to real-world scenarios
        4. Has a clear, definitive answer
        
        Format the question as JSON exactly like this:
        {
            "question": "Clear, specific question text with context",
            "category": "Main topic category",
            "difficulty": "%s"
        }
        
        Return ONLY the JSON, no other text or explanation.
        """,
                safeRole, safeExperience, safeDifficulty,
                safeRoleContext,
                safeExperienceContext,
                safeDifficultyContext,
                safeDifficulty
        );
    }

    // Enhanced evaluation with detailed scoring
    public String buildEvaluationPrompt(String question, String answer, String role, String experience) {
        // Escape any % characters to prevent format errors
        String escapedAnswer = answer.replace("%", "%%");
        String escapedQuestion = question.replace("%", "%%");

        return String.format("""
        You are a senior technical interviewer evaluating a candidate's answer for a %s position.
        Experience Level: %s
        
        QUESTION:
        %s
        
        CANDIDATE'S ANSWER:
        %s
        
        Evaluate this answer thoroughly on:
        
        1. TECHNICAL ACCURACY (40%% weight):
           - Is the information correct?
           - Are there any technical errors?
           - Are key concepts explained properly?
        
        2. COMPLETENESS (25%% weight):
           - Did they cover all key points?
           - What important aspects are missing?
           - Is the depth appropriate for the experience level?
        
        3. COMMUNICATION (20%% weight):
           - Is it clear and well-structured?
           - Is the language professional?
           - Are examples used effectively?
        
        4. PRACTICAL KNOWLEDGE (15%% weight):
           - Are real-world applications mentioned?
           - Is there evidence of hands-on experience?
           - Are best practices discussed?
        
        IMPORTANT: Return ONLY valid JSON. Do not include any other text, markdown, or explanation.
        
        Provide structured feedback in this exact JSON format:
        {
            "score": 7,
            "strengths": ["Strength 1", "Strength 2"],
            "weaknesses": ["Weakness 1", "Weakness 2"],
            "topicsToRevise": ["Topic 1", "Topic 2"],
            "modelAnswer": "Comprehensive model answer",
            "followUpQuestions": ["Q1", "Q2"],
            "overallAssessment": "Brief assessment"
        }
        
        Scoring Guide:
        9-10: Exceptional - comprehensive, accurate, and insightful
        7-8: Good - covers main points well, minor gaps
        5-6: Adequate - basic understanding, missing several key points
        3-4: Below expectations - significant gaps in understanding
        1-2: Needs improvement - incorrect or very limited understanding
        
        Return ONLY the JSON, no other text.
        """,
                role, experience,
                escapedQuestion,
                escapedAnswer
        );
    }

    private String getRoleContext(String role) {
        return switch (role.toLowerCase()) {
            case "java developer" -> """
            Focus on:
            - Core Java (Collections, Concurrency, Streams)
            - Spring Boot (IoC, AOP, Data JPA)
            - Microservices architecture
            - REST APIs and Web Services
            - Database optimization
            """;
            case "react developer" -> """
            Focus on:
            - React hooks and lifecycle
            - State management (Redux, Context)
            - Component design patterns
            - Performance optimization
            - Testing (Jest, React Testing Library)
            """;
            case "full stack developer" -> """
            Focus on:
            - Frontend technologies (React/Angular)
            - Backend technologies (Spring Boot/Node.js)
            - Database design (SQL/NoSQL)
            - API design and integration
            - DevOps basics
            """;
            default -> """
            Focus on:
            - Core concepts for the role
            - Practical problem-solving
            - Industry best practices
            - Architecture and design patterns
            """;
        };
    }

    private String getExperienceContext(String experience) {
        return switch (experience.toUpperCase()) {
            case "JUNIOR" -> """
                Expectation: 0-2 years experience
                Questions should test:
                - Basic concepts and fundamentals
                - Understanding of core technologies
                - Ability to write clean code
                - Basic problem-solving skills
                """;
            case "MID" -> """
                Expectation: 3-5 years experience
                Questions should test:
                - Deep understanding of core concepts
                - Ability to design solutions
                - Debugging and optimization skills
                - Experience with best practices
                """;
            case "SENIOR" -> """
                Expectation: 5+ years experience
                Questions should test:
                - Architecture and system design
                - Performance optimization
                - Mentoring and leadership
                - Complex problem-solving
                - Technology selection and trade-offs
                """;
            default -> """
                Questions should be appropriate for the experience level.
                """;
        };
    }

    private String getDifficultyContext(String difficulty) {
        return switch (difficulty.toUpperCase()) {
            case "EASY" -> """
                Question should:
                - Test basic understanding
                - Be straightforward
                - Focus on fundamental concepts
                - Have a clear, simple answer
                """;
            case "MEDIUM" -> """
                Question should:
                - Require thinking and analysis
                - Test application of concepts
                - Include a scenario or context
                - Have a moderate complexity answer
                """;
            case "HARD" -> """
                Question should:
                - Be challenging and complex
                - Test deep understanding
                - Include edge cases
                - Require problem-solving skills
                - Have a detailed, nuanced answer
                """;
            default -> """
                Question should be appropriate for the difficulty level.
                """;
        };
    }
}