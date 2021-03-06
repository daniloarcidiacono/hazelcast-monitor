# Hazelcast Monitor
This is an unofficial monitoring tool for Hazelcast.

![Dashboard](images/dashboard.png "Dashboard")

![Topic](images/topics.png "Topics")

## Modules
* `hazelcast-monitor-agent`: the main agent, that pulls data from the Hazelcast cluster;
* `hazelcast-monitor-starter-spring`: integration module between the agent and Spring Framework; 
* `hazelcast-monitor-sample-app`: an example application that uses Hazelcast;
* `hazelcast-monitor-webapp`: the front-end application of the monitor;

## How to compile the webapp
The webapp is written in Angular 6, therefore NodeJS must be installed to compile it.

```
cd hazelcast-monitor-webapp
npm install
npm run start
```

or 

```
npm run start-ssl
```

to serve in SSL.