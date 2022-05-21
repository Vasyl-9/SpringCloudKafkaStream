package com.vasyl.kafka.windowcount.service;

import com.vasyl.kafka.windowcount.model.SimpleInvoice;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class InvoiceListenerService {

    @Bean
    public Consumer<KStream<String, SimpleInvoice>> invoice() {
        return input -> input
                .peek((k, v) -> log.info("Key = " + k + " Created Time = "
                        + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)))
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .foreach((k, v) -> log.info("StoreID: " + k.key() +
                        " Window start: " + Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC) +
                        " Window end: " + Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC) +
                        " Count: " + v + " Window#: " + k.window().hashCode()));
    }
}
