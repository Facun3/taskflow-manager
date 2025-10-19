package com.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for TaskFlow API
 * 
 * This is the entry point of our Spring Boot application.
 * The @SpringBootApplication annotation enables:
 * - @Configuration: Marks this class as a source of bean definitions
 * - @EnableAutoConfiguration: Enables Spring Boot auto-configuration
 * - @ComponentScan: Scans for components in the current package and sub-packages
 */
@SpringBootApplication
public class TaskFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskFlowApplication.class, args);
    }
}

