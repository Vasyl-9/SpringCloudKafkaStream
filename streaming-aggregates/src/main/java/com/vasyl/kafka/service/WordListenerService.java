package com.vasyl.kafka.service;

import java.util.Arrays;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class WordListenerService {

    @Bean
    public Consumer<KStream<String, String>> words() {
        return input -> input.flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")))
                .groupBy((key, value) -> value)
                .count()
                .toStream()
                .peek((k, v) -> log.info("Word: {} Count: {}", k, v));
    }
}
