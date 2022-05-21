package com.vasyl.kafka.top3spots.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vasyl.kafka.top3spots.model.AdClick;
import com.vasyl.kafka.top3spots.model.AdInventories;
import com.vasyl.kafka.top3spots.model.ClicksByNewsType;
import com.vasyl.kafka.top3spots.model.Top3NewsTypes;
import java.util.function.BiConsumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ClicksListenerService {

    @Bean
    public BiConsumer<GlobalKTable<String, AdInventories>, KStream<String, AdClick>> adListener() {
        return (inventory, click) ->
        {
            click.foreach((k, v) -> log.info("Click Key: {}, Value: {}", k, v));

            KTable<String, Long> clicksByNewsTypeKTable = click.join(inventory,
                            (clickKey, clickValue) -> clickKey,
                            (clickValue, inventoryValue) -> inventoryValue)
                    .groupBy((joinedKey, joinedValue) -> joinedValue.getNewsType(),
                            Grouped.with(Serdes.String(),
                                    new JsonSerde<>(AdInventories.class)))
                    .count();

            clicksByNewsTypeKTable.groupBy((k, v) -> {
                                ClicksByNewsType value = new ClicksByNewsType();
                                value.setNewsType(k);
                                value.setClicks(v);
                                return KeyValue.pair("top3NewsTypes", value);
                            },
                            Grouped.with(Serdes.String(), new JsonSerde<>(ClicksByNewsType.class)))
                    .aggregate(Top3NewsTypes::new, (k, v, aggV) -> {
                        aggV.add(v);
                        return aggV;
                    }, (k, v, aggV) -> {
                        aggV.remove(v);
                        return aggV;
                    }, Materialized.<String, Top3NewsTypes, KeyValueStore<Bytes, byte[]>>
                                    as("top3-clicks")
                            .withKeySerde(Serdes.String())
                            .withValueSerde(new JsonSerde<>(Top3NewsTypes.class)))
                    .toStream()
                    .foreach((k, v) -> {
                        try {
                            log.info("k=" + k + " v= " + v.getTop3Sorted());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
        };
    }
}
