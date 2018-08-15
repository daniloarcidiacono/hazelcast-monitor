package it.xdnl.hazelcast.monitor.agent.helper;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.ClientConnectionListener;
import it.xdnl.hazelcast.monitor.agent.dto.response.SubscriptionNoticeResponse;
import it.xdnl.hazelcast.monitor.agent.product.Product;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;
import it.xdnl.hazelcast.monitor.agent.producer.AbstractTopicProducer;
import it.xdnl.hazelcast.monitor.agent.producer.TopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ConnectionSubscriptionsRegistry implements TopicListener, ClientConnectionListener {
    private static class Subscription {
        private final long id;
        private final ClientConnection connection;
        private final AbstractTopicProducer producer;

        public Subscription(long id, ClientConnection connection, AbstractTopicProducer producer) {
            this.id = id;
            this.connection = connection;
            this.producer = producer;
        }

        public long getId() {
            return id;
        }

        public ClientConnection getConnection() {
            return connection;
        }

        public AbstractTopicProducer getProducer() {
            return producer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Subscription that = (Subscription) o;
            return id == that.id &&
                    Objects.equals(connection, that.connection) &&
                    Objects.equals(producer, that.producer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, connection, producer);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ConnectionSubscriptionsRegistry.class);
    private Map<Long, Subscription> subscriptions = new HashMap<>();
    private Map<ClientConnection, Set<AbstractTopicProducer>> subscriptionConnectionIndex = new HashMap<>();
    private Map<AbstractTopicProducer, Set<ClientConnection>> subscriptionProducerIndex = new HashMap<>();
    private long subId = 0;

    public synchronized long subscribe(final ClientConnection connection, final AbstractTopicProducer topic) {
        // Register the subscription
        final Subscription subscription = new Subscription(
                subId++,
                connection,
                topic
        );
        subscriptions.put(subscription.getId(), subscription);

        // Update the indicies
        if (!subscriptionConnectionIndex.containsKey(connection)) {
            subscriptionConnectionIndex.put(connection, new HashSet<>());
        }

        if (!subscriptionProducerIndex.containsKey(topic)) {
            subscriptionProducerIndex.put(topic, new HashSet<>());
        }

        final Set<AbstractTopicProducer> connectionSubs = subscriptionConnectionIndex.get(connection);
        final Set<ClientConnection> topicSubs = subscriptionProducerIndex.get(topic);
        connectionSubs.add(subscription.getProducer());
        topicSubs.add(subscription.getConnection());

        // Enable the listeners
        topic.addListener(this);
        connection.addListener(this);
        return subscription.getId();
    }

    public synchronized void unsubscribe(final long subscriptionId) {
        logger.debug("Unsubscribing {}", subscriptionId);

        final Subscription subscription = subscriptions.get(subscriptionId);
        if (subscription == null) {
            logger.warn("Subscription {} not found", subscriptionId);
            return;
        }
        final AbstractTopicProducer producer = subscription.getProducer();
        final ClientConnection connection = subscription.getConnection();

        // Stop the producer
        producer.stop();

        final Set<AbstractTopicProducer> connectionTopics = subscriptionConnectionIndex.get(connection);
        final Set<ClientConnection> topicConnections = subscriptionProducerIndex.get(producer);
        topicConnections.remove(subscription.getConnection());
        connectionTopics.remove(subscription.getProducer());

        if (topicConnections.isEmpty()) {
            producer.removeListener(this);
            subscriptionProducerIndex.remove(producer);
            logger.debug("Producer {} has no more listeners, removing...", subscriptionId);
        }

        if (connectionTopics.isEmpty()) {
            connection.removeListener(this);
            subscriptionConnectionIndex.remove(connection);
            logger.debug("Connection {} has no more producers, removing...", subscriptionId);
        }

        subscriptions.remove(subscription.getId());
    }

    public synchronized void unsubscribeAll(final ClientConnection connection) {
        logger.debug("Removing connection");
        final Set<AbstractTopicProducer> connectionSubs = subscriptionConnectionIndex.get(connection);
        if (connectionSubs == null) {
            logger.warn("Connection not found");
            return;
        }
        connectionSubs.forEach(AbstractTopicProducer::stop);

        for (AbstractTopicProducer producer : connectionSubs) {
            final Set<ClientConnection> topicSubs = subscriptionProducerIndex.get(producer);
            topicSubs.remove(connection);

            if (topicSubs.isEmpty()) {
                producer.removeListener(this);
                subscriptionProducerIndex.remove(producer);
                logger.debug("Producer {} has no more listeners, removing...");
            }
        }

        final Set<Long> subscriptionsToRemove = new HashSet<>();
        for (Subscription subscription : subscriptions.values()) {
            if (subscription.getConnection().equals(connection)) {
                subscriptionsToRemove.add(subscription.getId());
            }
        }

        for (Long id : subscriptionsToRemove) {
            logger.debug("Removing subscription {}", id);
            subscriptions.remove(id);
        }

        connection.removeListener(this);
        subscriptionConnectionIndex.remove(connection);
    }

    @Override
    public synchronized void notice(final AbstractTopicProducer topic, final Product product) {
        final SubscriptionNoticeResponse notice = new SubscriptionNoticeResponse();
        notice.setTopicType(topic.getTopicType());
        notice.setNotice(product);

        for (Map.Entry<Long, Subscription> entry : subscriptions.entrySet()) {
            if (entry.getValue().getProducer().equals(topic)) {
                notice.setSubscriptionId(entry.getKey());
                ClientConnectionUtils.convertAndSend(entry.getValue().getConnection(), notice);
            }
        }

//        for (ClientConnection connection : subscriptionProducerIndex.get(topic)) {
//            ClientConnectionUtils.convertAndSend(connection, notice);
//        }
    }

    @Override
    public synchronized void closed(final ClientConnection connection) {
        unsubscribeAll(connection);
    }
}
