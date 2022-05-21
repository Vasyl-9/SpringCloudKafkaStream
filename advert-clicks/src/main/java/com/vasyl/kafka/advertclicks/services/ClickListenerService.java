package com.vasyl.kafka.advertclicks.services;

import com.vasyl.kafka.advertclicks.model.AdClick;
import com.vasyl.kafka.advertclicks.model.AdInventories;
import java.util.function.BiConsumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ClickListenerService {

    @Bean
    public BiConsumer<GlobalKTable<String, AdInventories>, KStream<String, AdClick>> adListener() {
        return (inventory, click) ->
        {
            click.foreach((k, v) -> log.info("Click Key: {}, Value: {}", k, v));

            click.join(inventory,
                            (clickKey, clickValue) -> clickKey,
                            (clickValue, inventoryValue) -> inventoryValue)
                    .groupBy((joinedKey, joinedValue) -> joinedValue.getNewsType(),
                            Grouped.with(Serdes.String(),
                                    new JsonSerde<>(AdInventories.class)))
                    .count()
                    .toStream()
                    .foreach((k, v) -> log.info("Click Key: {}, Value: {}", k, v));
        };
    }
}
