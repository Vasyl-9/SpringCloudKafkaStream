package com.vasyl.kafka.avroposfanout.services;

import com.vasyl.kafka.avroposfanout.model.HadoopRecord;
import com.vasyl.kafka.model.PosInvoice;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class HadoopRecordProcessorService {

    @Autowired
    RecordBuilder recordBuilder;

    @Bean
    public Function<KStream<String, PosInvoice>, KStream<String, HadoopRecord>> hadoop() {
        return input -> input
                .mapValues( v -> recordBuilder.getMaskedInvoice(v))
                .flatMapValues( v -> recordBuilder.getHadoopRecords(v))
                .peek((k, v) -> log.info(String.format("Hadoop Record:- Key: %s, Value: %s", k, v)));
    }
}