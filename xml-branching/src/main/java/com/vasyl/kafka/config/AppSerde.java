package com.vasyl.kafka.config;

import com.vasyl.kafka.model.OrderEnvelop;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import java.util.Collections;
import java.util.Map;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Service;

@Service
public class AppSerde extends Serdes {

    private static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
    private static final Map<String, String> serdeConfig = Collections.singletonMap(
            "schema.registry.url", SCHEMA_REGISTRY_URL);

    public static Serde<OrderEnvelop> orderEnvelop() {
        final Serde<OrderEnvelop> specificJsonSerde = new KafkaJsonSchemaSerde<>();
        specificJsonSerde.configure(serdeConfig, false);
        return specificJsonSerde;
    }
}
