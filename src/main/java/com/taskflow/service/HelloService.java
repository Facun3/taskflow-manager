package com.taskflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Hello Service
 * 
 * This service demonstrates:
 * - Service layer pattern
 * - Business logic separation
 * - Spring's @Service annotation for component scanning
 * - SLF4J logging
 */
@Service
public class HelloService {

    private static final Logger log = LoggerFactory.getLogger(HelloService.class);

    /**
     * Gets a basic greeting message
     * 
     * @return greeting message
     */
    public String getGreeting() {
        log.debug("Generating basic greeting");
        return "Hello from TaskFlow API! Welcome to our task management system.";
    }

    /**
     * Gets a personalized greeting message
     * 
     * @param name the name to personalize the greeting
     * @return personalized greeting message
     */
    public String getPersonalizedGreeting(String name) {
        log.debug("Generating personalized greeting for: {}", name);
        
        if (name == null || name.trim().isEmpty()) {
            return getGreeting();
        }
        
        return String.format("Hello %s! Welcome to TaskFlow API. Ready to manage your tasks?", name.trim());
    }

    /**
     * Gets application information
     * 
     * @return application info
     */
    public String getApplicationInfo() {
        log.debug("Getting application information");
        return "TaskFlow API v0.0.1-SNAPSHOT - A Spring Boot learning project";
    }
}
