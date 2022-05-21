package com.vasyl.kafka.sessionwindow.service;

import com.vasyl.kafka.sessionwindow.models.UserClick;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Log4j2
@Service
public class ClickListenerService {

    @Bean
    public Consumer<KStream<String, UserClick>> click() {
        return input -> input
                .peek((k, v) -> log.info("Key = " + k + " Created Time = "
                        + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)))
                .groupByKey()
                .windowedBy(SessionWindows.with(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .foreach((k, v) -> log.info("UserID: " + k.key() +
                        " Window start: " + Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC) +
                        " Window end: " + Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC) +
                        " Count: " + v + " Window#: " + k.window().hashCode()));
    }
}
