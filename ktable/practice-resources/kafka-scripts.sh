confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic stock-tick-topic

kafka-console-producer --topic stock-tick-topic --broker-list localhost:9092 --property parse.key=true --property key.separator=":"

confluent local services stop
confluent local destroy