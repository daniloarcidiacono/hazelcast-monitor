package it.xdnl.hazelcast.monitor.agent.factory;

import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.topic.*;
import it.xdnl.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import it.xdnl.hazelcast.monitor.agent.producer.*;
import it.xdnl.hazelcast.monitor.agent.query.PredicateQueryEngine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopicProducerFactory {
    private ScheduledExecutorService threadPool;
    private ConnectionSubscriptionsRegistry connectionSubscriptionsRegistry;
    private PredicateQueryEngine predicateQueryEngine;

    public TopicProducerFactory(final int corePoolSize) {
        threadPool = Executors.newScheduledThreadPool(corePoolSize);
    }

    public AbstractTopicProducer instanceTopicProducer(final SubscribeRequest message) {
        if (message.getTopic() instanceof ClustersTopic) {
            return wrapProducer(new ClustersTopicProducer(), message);
        }

        if (message.getTopic() instanceof InternalsTopic) {
            final InternalsTopic topic = (InternalsTopic)message.getTopic();
            return wrapProducer(new InternalsTopicProducer(topic.getInstanceName(), connectionSubscriptionsRegistry), message);
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
            final DistributedObjectTopic topic = (DistributedObjectTopic) message.getTopic();

            // Topics have a dedicated producer, because they only emit messages when a
            // message is received; so they are not wrapped by ScheduledTopicProducer
            if (topic.getDistributedObjectType().equals(DistributedObjectType.TOPIC)) {
                return new TopicTopicProducer(topic.getInstanceName(), topic.getObjectName(), predicateQueryEngine);
            }

            return wrapProducer(new DistributedObjectTopicProducer(topic.getInstanceName(), topic.getDistributedObjectType(), topic.getObjectName(), predicateQueryEngine), message);
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

    public ConnectionSubscriptionsRegistry getConnectionSubscriptionsRegistry() {
        return connectionSubscriptionsRegistry;
    }

    public void setConnectionSubscriptionsRegistry(ConnectionSubscriptionsRegistry connectionSubscriptionsRegistry) {
        this.connectionSubscriptionsRegistry = connectionSubscriptionsRegistry;
    }

    public PredicateQueryEngine getPredicateQueryEngine() {
        return predicateQueryEngine;
    }

    public void setPredicateQueryEngine(PredicateQueryEngine predicateQueryEngine) {
        this.predicateQueryEngine = predicateQueryEngine;
    }
}
