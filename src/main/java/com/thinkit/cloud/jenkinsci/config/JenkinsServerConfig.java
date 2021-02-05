package com.thinkit.cloud.jenkinsci.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.offbytwo.jenkins.JenkinsServer;

@Configuration
@Component
public class JenkinsServerConfig {
	
	@Value("${jenkins_url}")
    private  String jenkins;

	@Value("${jenkins_username}")
    private  String name;
	@Value("${jenkins_password}")
    private  String password;
	
    @Bean
    public JenkinsServer getJenkinsServer() {
        JenkinsServer jenkinsServer = null;

        try {
            jenkinsServer = new JenkinsServer(new URI(jenkins), name, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jenkinsServer;
    }
}