package com.vasyl.kafka.otpvalidation.config;

import com.vasyl.kafka.otpvalidation.model.PaymentRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class PaymentRequestTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        PaymentRequest request = (PaymentRequest) record.value();
        return ((request.getCreatedTime() > 0) ? request.getCreatedTime() : partitionTime);
    }

    @Bean TimestampExtractor requestTimeExtractor() {
        return new PaymentRequestTimeExtractor();
    }
}
