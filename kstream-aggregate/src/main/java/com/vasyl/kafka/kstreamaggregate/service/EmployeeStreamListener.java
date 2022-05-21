package com.vasyl.kafka.kstreamaggregate.service;

import com.vasyl.kafka.model.Employee;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
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
        return input -> input.peek((k, v) -> log.info("Key: {}, Value:{}", k, v))
                .groupBy((k, v) -> v.getDepartment())
                .aggregate(
                        () -> recordBuilder.init(),
                        (k, v, aggV) -> recordBuilder.aggregate(v, aggV)
                ).toStream()
                .peek((k, v) -> log.info("Key = " + k + " Value = " + v.toString()));
    }
}
