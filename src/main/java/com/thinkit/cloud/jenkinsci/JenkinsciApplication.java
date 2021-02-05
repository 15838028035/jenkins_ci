package com.thinkit.cloud.jenkinsci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JenkinsciApplication {
    public static void main(String[] args) {
        SpringApplication.run(JenkinsciApplication.class, args);
    }
}
