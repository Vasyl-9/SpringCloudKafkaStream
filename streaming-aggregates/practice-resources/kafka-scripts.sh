confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streaming-words-topic

kafka-console-producer --topic streaming-words-topic --broker-list localhost:9092

confluent local services stop
confluent local destroy