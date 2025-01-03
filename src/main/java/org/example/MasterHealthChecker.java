package org.example;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MasterHealthChecker {

    private final RestTemplate restTemplate;
    private final ReplicationConfig replicationConfig;

    public MasterHealthChecker(RestTemplate restTemplate, ReplicationConfig replicationConfig) {
        this.restTemplate = restTemplate;
        this.replicationConfig = replicationConfig;
    }

    @Scheduled(fixedRate = 5000)
    public void checkMasterHealth() {
        if ("slave".equals(replicationConfig.getRole())) {
            try {
                restTemplate.getForObject(replicationConfig.getMasterUrl() + "/database/role", String.class);
            } catch (Exception e) {
                System.out.println("Master unreachable. Electing new master.");
                replicationConfig.setMasterUrl("http://localhost:" + replicationConfig.getRole());
            }
        }
    }
}

