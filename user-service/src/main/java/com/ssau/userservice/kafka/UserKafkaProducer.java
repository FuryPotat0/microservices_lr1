package com.ssau.userservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserKafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topic.company-deleted-user}")
    private String companyDeletedUserTopic;

    public void sendDeleteCompanyUsersMessage(String companyId) {
        kafkaTemplate.send(companyDeletedUserTopic, companyId);
    }
}
