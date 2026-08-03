package com.interviewprep.util;

import com.interviewprep.dto.response.FeedbackResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackAnalyzer {

    public String getOverallRating(int score) {
        if (score >= 9) return "🌟 Exceptional - Excellent answer!";
        if (score >= 7) return "✅ Good - Solid understanding";
        if (score >= 5) return "📖 Adequate - Needs more depth";
        if (score >= 3) return "⚠️ Below expectations - Significant gaps";
        return "🔴 Needs improvement - Review fundamentals";
    }

    public String getConfidenceLevel(int score) {
        if (score >= 7) return "HIGH";
        if (score >= 4) return "MEDIUM";
        return "LOW";
    }

    public List<String> getPriorityTopics(List<String> topics) {
        // Return top 3 topics with highest priority
        return topics.stream().limit(3).toList();
    }

    public String generateStudyPlan(List<String> weaknesses, List<String> topicsToRevise) {
        StringBuilder plan = new StringBuilder();
        plan.append("📚 Recommended Study Plan:\n\n");

        int day = 1;
        for (String topic : topicsToRevise) {
            plan.append("Day ").append(day++).append(": Review ").append(topic).append("\n");
        }

        for (String weakness : weaknesses) {
            plan.append("Day ").append(day++).append(": Practice ").append(weakness).append("\n");
        }

        plan.append("\n💡 Tip: Practice with real-world examples and mock interviews.");
        return plan.toString();
    }
}