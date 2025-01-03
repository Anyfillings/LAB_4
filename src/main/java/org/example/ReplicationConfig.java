package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ReplicationConfig {

    @Value("${server.role}")
    private String role;

    @Value("${replication.hosts}")
    private String hosts;

    private String masterUrl;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getReplicationHosts() {
        return Arrays.asList(hosts.split(","));
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }
}