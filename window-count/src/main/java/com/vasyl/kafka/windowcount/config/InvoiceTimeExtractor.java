package com.vasyl.kafka.windowcount.config;

import com.vasyl.kafka.windowcount.model.SimpleInvoice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InvoiceTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        SimpleInvoice invoice = (SimpleInvoice) record.value();
        return  ((invoice.getCreatedTime() > 0) ? invoice.getCreatedTime() : partitionTime);
    }

    @Bean
    public TimestampExtractor invoiceTimesExtractor() {
        return new InvoiceTimeExtractor();
    }
}
