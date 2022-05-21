package com.vasyl.kafka.ktableaggregate.service;

import com.vasyl.kafka.model.Employee;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EmployeeStreamListener {

    @Autowired
    RecordBuilder recordBuilder;

    @Bean
    public Consumer<KStream<String, Employee>> employee(){
        return input -> input
                .map((k, v) -> KeyValue.pair(v.getId(), v))
                .peek((k, v) -> log.info("Key: {}, Value:{}", k, v))
                .toTable()
                .groupBy((k, v) -> KeyValue.pair(v.getDepartment(), v))
                .aggregate(
                        () -> recordBuilder.init(),
                        // add value
                        (k, v, aggV) -> recordBuilder.aggregate(v, aggV),
                        // update value
                        (k, v, aggV) -> recordBuilder.subtract(v, aggV)
                ).toStream()
                .peek((k, v) -> log.info("Key = " + k + " Value = " + v.toString()));
    }
}
