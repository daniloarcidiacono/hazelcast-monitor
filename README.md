# Hazelcast Monitor
This is an unofficial monitoring tool for Hazelcast, featuring:

* Thorough inspection of maps/multi-maps/replicated maps (no need to "guess" the keys!), atomic longs, atomic references, caches, cardinality estimators, count-down latches, executors, lists, locks, queues, ringbuffers, semaphores, sets and topics;
* Statistics for caches, executors, maps, queues and topics;
* Data filtering with JavaScript conditions;
* Data slicing with JSONPath expressions;
* Plain Java integration (uses Java-WebSocket library);
* Spring Boot integration;

# Images
![Dashboard](images/dashboard.png "Dashboard")

![Data filtering and slicing](images/data_filtering_slicing.png "Data filtering and slicing")

![Statistics](images/statistics_2.png "Statistics")

![Topic](images/topics.png "Topics")

## Modules
* `hazelcast-monitor-agent`: the main agent, that pulls data from the Hazelcast cluster;
* `hazelcast-monitor-starter-spring`: integration module with Spring Boot;
* `hazelcast-monitor-starter-java-websocket`: integration module with Java-WebSocket; 
* `hazelcast-monitor-sample-app`: an example application using Hazelcast;
* `hazelcast-monitor-webapp`: the front-end application of the monitor;
