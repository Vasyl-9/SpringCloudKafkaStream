confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user-clicks-topic

kafka-console-producer --broker-list localhost:9092 --topic user-clicks-topic \
--property parse.key=true --property key.separator=":"

confluent local destroy