package com.ssau.companyservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CompanyKafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topic.company-deleted}")
    private String companyDeletedTopic;

    public void sendDeleteCompanyMessage(String companyId) {
        kafkaTemplate.send(companyDeletedTopic, companyId);
    }
}
