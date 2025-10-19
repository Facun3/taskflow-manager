package com.taskflow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

/**
 * Application Configuration
 * 
 * This configuration class demonstrates:
 * - @Configuration annotation for Spring configuration
 * - @Bean methods for defining Spring beans
 * - @Profile annotation for environment-specific configuration
 * - Dependency Injection configuration
 */
@Configuration
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    /**
     * Clock bean for time operations
     * This demonstrates how to create custom beans in Spring
     * 
     * @return Clock instance
     */
    @Bean
    public Clock clock() {
        log.info("Creating Clock bean");
        return Clock.systemDefaultZone();
    }

    /**
     * Development-specific configuration
     * This bean is only created when the 'dev' profile is active
     * 
     * @return development configuration message
     */
    @Bean
    @Profile("dev")
    public String devConfiguration() {
        log.info("Development profile is active - enabling dev features");
        return "Development configuration loaded";
    }

    /**
     * Production-specific configuration
     * This bean is only created when the 'prod' profile is active
     * 
     * @return production configuration message
     */
    @Bean
    @Profile("prod")
    public String prodConfiguration() {
        log.info("Production profile is active - enabling production features");
        return "Production configuration loaded";
    }
}
