package com.vasyl.kafka.rewards.services;

import com.vasyl.kafka.model.Notification;
import com.vasyl.kafka.model.PosInvoice;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LoyaltyService {

    @Autowired
    RecordBuilder recordBuilder;

    @Bean
    public Function<KStream<String, PosInvoice>, KStream<String, Notification>> invoiceToNotification() {
        return input -> input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase("PRIME"))
                .map((k, v) -> new KeyValue<>(v.getCustomerCardNo(), recordBuilder.getNotification(v)))
                .groupByKey()
                .reduce((aggValue, newValue) -> {
                    newValue.setTotalLoyaltyPoints(newValue.getEarnedLoyaltyPoints() + aggValue.getTotalLoyaltyPoints());
                    return newValue;
                })
                .toStream()
                .peek((k, v) -> log.info(String.format("Notification:- Key: %s, Value: %s", k, v)));
    }
}
