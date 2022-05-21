confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic employees-topic

kafka-avro-console-producer --broker-list localhost:9092 --topic employees-topic \
--property value.schema='{"namespace": "com.vasyl.kafka.model","type": "record","name": "Employee","fields": [{"name": "id","type": "string"},{"name": "name","type": "string"},{"name": "department","type": "string"},{"name": "salary","type":"int"}]}'


confluent local destroy