package com.thinkit.cloud.jenkinsci.bean;


import com.offbytwo.jenkins.client.JenkinsHttpClient;

public class BaseModel {
    JenkinsHttpClient client;

    public JenkinsHttpClient getClient() {
        return client;
    }

    public void setClient(JenkinsHttpClient client) {
        this.client = client;
    }
}