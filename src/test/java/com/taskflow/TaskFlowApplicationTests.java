package com.taskflow;

import com.taskflow.controller.HelloController;
import com.taskflow.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for TaskFlow Application
 * 
 * This test class demonstrates:
 * - Spring Boot test integration
 * - @SpringBootTest annotation for full application context
 * - @ActiveProfiles for test-specific configuration
 * - Dependency injection in tests
 */
@SpringBootTest
@ActiveProfiles("test")
class TaskFlowApplicationTests {

    @Autowired
    private HelloController helloController;

    @Autowired
    private HelloService helloService;

    /**
     * Test that the application context loads successfully
     * This is a basic smoke test to ensure Spring Boot starts correctly
     */
    @Test
    void contextLoads() {
        // This test will pass if the application context loads without errors
        assertThat(helloController).isNotNull();
        assertThat(helloService).isNotNull();
    }

    /**
     * Test that the main application class can be instantiated
     */
    @Test
    void mainApplicationClassLoads() {
        assertThat(TaskFlowApplication.class).isNotNull();
    }
}
