package com.vasyl.kafka.avroposfanout.services;

import com.vasyl.kafka.avroposfanout.model.Notification;
import com.vasyl.kafka.model.PosInvoice;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class NotificationProcessorService {

    @Autowired
    RecordBuilder recordBuilder;

    @Bean
    public Function<KStream<String, PosInvoice>, KStream<String, Notification>> notification() {
        return input -> input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase("prime"))
                .mapValues(v -> recordBuilder.getNotification(v))
                .peek((k, v) -> log.info(String.format("Notification:- Key: %s, Value: %s", k, v)));
    }
}
