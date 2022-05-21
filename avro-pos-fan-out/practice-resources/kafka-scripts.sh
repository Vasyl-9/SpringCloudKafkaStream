# Connect to avroposgen
confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic avro-pos-topic

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic input-topic-dlq
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic loyalty-topic
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic hadoop-sink-topic

kafka-avro-console-consumer --bootstrap-server localhost:9092 --from-beginning --property print.key=true --property key.separator=":" --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --topic avro-pos-topic
kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --property print.key=true --property key.separator=":" --topic loyalty-topic
kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --property print.key=true --property key.separator=":" --topic hadoop-sink-topic

confluent local services stop
confluent local destroy