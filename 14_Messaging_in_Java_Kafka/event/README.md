1. windows/zookeeper-server-start.bat ~/Programs/kafka/kafka/config/zookeeper.properties
2. windows/kafka-server-start.bat ~/Programs/kafka/kafka/config/server.properties

create a topic : windows/kafka-topics.bat --create  --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic create-event-notification

get topic details : windows/kafka-topics.bat kafka-topics.sh --describe --topic create-event-notification --bootstrap-server localhost:9092
 
get list of all topics :  windows/kafka-topics.bat --list --bootstrap-server localhost:9092

sent event to the topic : windows/kafka-console-producer.bat --broker-list localhost:9092 --topic create-event-notification

get event form topic : windows/kafka-console-consumer.bat --topic create-event-notification --from-beginning --bootstrap-server localhost:9092

