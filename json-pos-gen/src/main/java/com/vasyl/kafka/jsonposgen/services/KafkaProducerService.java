package com.vasyl.kafka.jsonposgen.services;

import com.vasyl.kafka.jsonposgen.model.PosInvoice;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaProducerService {

    @Value("${application.configs.topic.name}")
    private String TOPIC_NAME;

    private final KafkaTemplate<String, PosInvoice> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, PosInvoice> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(PosInvoice invoice) {
        log.info(String.format("Producing Invoic No: %s", invoice.getInvoiceNumber()));
        kafkaTemplate.send(TOPIC_NAME, invoice.getStoreID(), invoice);
    }
}
