package com.taskflow.controller;

import com.taskflow.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Hello World Controller
 * 
 * This controller demonstrates:
 * - REST endpoint creation with @RestController
 * - Dependency Injection with @Autowired
 * - SLF4J logging
 * - Request mapping and parameters
 * - JSON response handling
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    private final HelloService helloService;

    /**
     * Constructor injection - Spring's preferred way of dependency injection
     * This demonstrates the Dependency Injection pattern
     */
    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
        log.info("HelloController initialized with HelloService");
    }

    /**
     * Simple GET endpoint that returns a greeting message
     * 
     * @return ResponseEntity with greeting message
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> sayHello() {
        log.debug("Hello endpoint called");
        
        String message = helloService.getGreeting();
        
        Map<String, Object> response = Map.of(
            "message", message,
            "timestamp", LocalDateTime.now(),
            "status", "success"
        );
        
        log.info("Hello response generated: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * GET endpoint with query parameter
     * 
     * @param name Optional name parameter
     * @return ResponseEntity with personalized greeting
     */
    @GetMapping("/personalized")
    public ResponseEntity<Map<String, Object>> sayHelloPersonalized(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        
        log.debug("Personalized hello endpoint called with name: {}", name);
        
        String message = helloService.getPersonalizedGreeting(name);
        
        Map<String, Object> response = Map.of(
            "message", message,
            "name", name,
            "timestamp", LocalDateTime.now(),
            "status", "success"
        );
        
        log.info("Personalized hello response generated: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     * 
     * @return ResponseEntity with application status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check endpoint called");
        
        Map<String, Object> response = Map.of(
            "status", "UP",
            "application", "TaskFlow API",
            "version", "0.0.1-SNAPSHOT",
            "timestamp", LocalDateTime.now()
        );
        
        return ResponseEntity.ok(response);
    }
}
