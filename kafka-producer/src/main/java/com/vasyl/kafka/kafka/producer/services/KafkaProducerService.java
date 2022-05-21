package com.vasyl.kafka.kafka.producer.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String key, String value){
        log.info(String.format("Producing Message- Key: %s, Value: %s to topic: %s", key, value, topic));
        kafkaTemplate.send(topic, key, value);
    }
}
