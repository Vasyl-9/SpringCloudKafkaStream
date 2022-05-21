confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic active-inventories

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic ad-clicks

kafka-console-producer --broker-list localhost:9092 --topic active-inventories \
--property parse.key=true --property key.separator=":"

kafka-console-producer --broker-list localhost:9092 --topic ad-clicks \
--property parse.key=true --property key.separator=":"

confluent local destroy