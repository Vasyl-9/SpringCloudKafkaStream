confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic payment_request

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic payment_confirmation

kafka-console-producer --broker-list localhost:9092 --topic payment_request \
--property parse.key=true --property key.separator=":"

kafka-console-producer --broker-list localhost:9092 --topic payment_confirmation \
--property parse.key=true --property key.separator=":"

confluent local destroy