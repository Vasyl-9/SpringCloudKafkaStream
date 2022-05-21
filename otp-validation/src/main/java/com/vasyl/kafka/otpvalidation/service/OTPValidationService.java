package com.vasyl.kafka.otpvalidation.service;

import com.vasyl.kafka.otpvalidation.model.PaymentConfirmation;
import com.vasyl.kafka.otpvalidation.model.PaymentRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.function.BiConsumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OTPValidationService {

    @Autowired
    private RecordBuilder recordBuilder;

    @Bean
    public BiConsumer<KStream<String, PaymentRequest>, KStream<String, PaymentConfirmation>> payment() {
        return (request, confirmation) ->
        {
            request.foreach((k, v) -> log.info("Request Key = " + k + " Created Time = "
                    + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)));

            confirmation.foreach((k, v) -> log.info("Confirmation Key = " + k + " Created Time = "
                    + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)));

            request.join(confirmation,
                    (r, c) -> recordBuilder.getTransactionStatus(r, c),
                    JoinWindows.of(Duration.ofMinutes(5)),
                    StreamJoined.with(Serdes.String(),
                            new JsonSerde<>(PaymentRequest.class),
                            new JsonSerde<>(PaymentConfirmation.class)))
                    .foreach((k, v) -> log.info("Transaction ID = " + k + " Status = " + v.getStatus()));
        };
    }
}
