package it.xdnl.hazelcast.monitor.agent.factory;

import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.topic.*;
import it.xdnl.hazelcast.monitor.agent.producer.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopicProducerFactory {
    private ScheduledExecutorService threadPool;

    public TopicProducerFactory(final int corePoolSize) {
        threadPool = Executors.newScheduledThreadPool(corePoolSize);
    }

    public AbstractTopicProducer instanceTopicProducer(final SubscribeRequest message) {
        if (message.getTopic() instanceof StatisticsTopic) {
            StatisticsTopic statisticsTopic = (StatisticsTopic)message.getTopic();
            return new ScheduledTopicProducer(
                new StatisticsTopicProducer(statisticsTopic.getInstanceName()),
                threadPool,
                message.getFrequency(),
                TimeUnit.SECONDS
            );
        }

        if (message.getTopic() instanceof MembersTopic) {
            MembersTopic membersTopic = (MembersTopic)message.getTopic();
            return new ScheduledTopicProducer(
                new MembersTopicProducer(membersTopic.getInstanceName()),
                threadPool,
                message.getFrequency(),
                TimeUnit.SECONDS
            );
        }

        if (message.getTopic() instanceof MapTopic) {
            MapTopic mapTopic = (MapTopic)message.getTopic();
            return new ScheduledTopicProducer(
                new MapTopicProducer(mapTopic.getInstanceName(), mapTopic.getMapName()),
                threadPool,
                message.getFrequency(),
                TimeUnit.SECONDS
            );
        }

        if (message.getTopic() instanceof ClustersTopic) {
            ClustersTopic clustersTopic = (ClustersTopic)message.getTopic();
            return new ScheduledTopicProducer(
                new ClustersTopicProducer(),
                threadPool,
                message.getFrequency(),
                TimeUnit.SECONDS
            );
        }

        if (message.getTopic() instanceof MapsTopic) {
            MapsTopic mapsTopic = (MapsTopic)message.getTopic();
            return new ScheduledTopicProducer(
                new MapsTopicProducer(mapsTopic.getInstanceName()),
                threadPool,
                message.getFrequency(),
                TimeUnit.SECONDS
            );
        }

        return null;
    }
}
