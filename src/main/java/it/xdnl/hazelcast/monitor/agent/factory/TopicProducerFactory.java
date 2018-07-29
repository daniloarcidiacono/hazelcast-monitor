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
        if (message.getTopic() instanceof ClustersTopic) {
            return wrapProducer(new ClustersTopicProducer(), message);
        }

        if (message.getTopic() instanceof StatisticsTopic) {
            final StatisticsTopic topic = (StatisticsTopic)message.getTopic();
            return wrapProducer(new StatisticsTopicProducer(topic.getInstanceName()), message);
        }

        if (message.getTopic() instanceof MembersTopic) {
            final MembersTopic topic = (MembersTopic)message.getTopic();
            return wrapProducer(new MembersTopicProducer(topic.getInstanceName()), message);
        }

        if (message.getTopic() instanceof DistributedObjectsTopic) {
            final DistributedObjectsTopic topic = (DistributedObjectsTopic)message.getTopic();
            return wrapProducer(new DistributedObjectsTopicProducer(topic.getInstanceName(), topic.getDistributedObjectType()), message);
        }

        if (message.getTopic() instanceof DistributedObjectTopic) {
            final DistributedObjectTopic topic = (DistributedObjectTopic)message.getTopic();
            return wrapProducer(new DistributedObjectTopicProducer(topic.getInstanceName(), topic.getDistributedObjectType(), topic.getObjectName()), message);
        }

        return null;
    }

    private ScheduledTopicProducer wrapProducer(final AbstractTopicProducer producer, final SubscribeRequest message) {
        return new ScheduledTopicProducer(
            producer,
            threadPool,
            message.getFrequency(),
            TimeUnit.SECONDS
        );
    }
}
