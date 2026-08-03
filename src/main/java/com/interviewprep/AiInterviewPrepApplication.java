package com.interviewprep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class AiInterviewPrepApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AiInterviewPrepApplication.class, args);

		// Debug: Check properties
		Environment env = context.getEnvironment();
		System.out.println("\n=== Configuration Debug ===");
		System.out.println("groq.api.url: " + env.getProperty("groq.api.url"));
		System.out.println("groq.api.key: " + (env.getProperty("groq.api.key") != null ? "SET" : "NOT SET"));
		System.out.println("groq.model: " + env.getProperty("groq.model"));
		System.out.println("groq.temperature: " + env.getProperty("groq.temperature"));
		System.out.println("groq.max-tokens: " + env.getProperty("groq.max-tokens"));
		System.out.println("===========================\n");

		System.out.println("✅ Application started successfully!");
		System.out.println("📡 Server running on: http://localhost:8080");
		System.out.println("🔍 Health check: http://localhost:8080/api/health");
	}
}