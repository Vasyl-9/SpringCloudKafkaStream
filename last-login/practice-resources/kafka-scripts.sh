confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user-master

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user-login

kafka-console-producer --broker-list localhost:9092 --topic user-master \
--property parse.key=true --property key.separator=":"

kafka-console-producer --broker-list localhost:9092 --topic user-login \
--property parse.key=true --property key.separator=":"

confluent local destroy