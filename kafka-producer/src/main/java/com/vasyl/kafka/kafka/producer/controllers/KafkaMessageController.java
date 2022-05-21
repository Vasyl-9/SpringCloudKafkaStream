package com.vasyl.kafka.kafka.producer.controllers;

import com.vasyl.kafka.kafka.producer.model.IncomingMessage;
import com.vasyl.kafka.kafka.producer.services.KafkaProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaMessageController {

    KafkaProducerService kafkaProducerService;

    public KafkaMessageController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/post")
    public String sendMessageToKafka(@RequestBody IncomingMessage message){
        kafkaProducerService.sendMessage(message.getTopic(), message.getKey(), message.getValue());
        return String.format("Success - Message for key %s is sent to Kafka Topic: %s", message.getKey(),
                message.getTopic());
    }
}
