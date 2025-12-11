package com.team3.workoutplanservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableDiscoveryClient
@EnableJpaAuditing
public class WorkoutplanServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkoutplanServiceApplication.class, args);
    }

}
