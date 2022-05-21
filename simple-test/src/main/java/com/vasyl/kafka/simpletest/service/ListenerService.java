package com.vasyl.kafka.simpletest.service;

import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ListenerService {

    @Bean
    public Function<KStream<String, String>, KStream<String, String>> process() {
        return input -> {
            input.foreach((k, v) -> log.info("Received Input: {}", v));
            return input.mapValues((ValueMapper<String, String>) String::toUpperCase);
        };
    }
}
