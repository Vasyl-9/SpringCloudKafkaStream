package com.vasyl.kafka.kafka.producer.model;

import lombok.Data;

@Data
public class IncomingMessage {

    private String topic;
    private String key;
    private String value;
}
