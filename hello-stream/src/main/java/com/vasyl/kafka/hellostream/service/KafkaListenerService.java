package com.vasyl.kafka.hellostream.service;

import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class KafkaListenerService {

    @Bean
    public Consumer<KStream<Object, String>> channelInput() {
        return input ->
                input.foreach((k,v) -> log.info(String.format("Key: %s, Value: %s", k, v)));
    }
}
