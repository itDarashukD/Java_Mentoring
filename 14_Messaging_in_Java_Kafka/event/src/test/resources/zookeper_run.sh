#!/bin/bash

# Start ZooKeeper
#zookeeper/bin/zkServer.sh start

c:/Users/Dzmitry_Darashuk/Programs/kafka/kafka/bin/windows/zookeeper-server-start.bat ~/Programs/kafka/kafka/config/zookeeper.properties start
c:/Users/Dzmitry_Darashuk/Programs/kafka/kafka/bin/windows/kafka-server-start.bat ~/Programs/kafka/kafka/config/server.properties

# Wait for ZooKeeper to start
#while ! nc -z localhost 2181; do
#  sleep 0.1
#done