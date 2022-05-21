confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic simple-invoice-topic

kafka-console-producer --broker-list localhost:9092 --topic simple-invoice-topic \
--property parse.key=true --property key.separator=":"

confluent local destroy