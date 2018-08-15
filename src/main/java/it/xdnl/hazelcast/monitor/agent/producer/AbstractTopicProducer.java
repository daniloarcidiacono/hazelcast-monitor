package it.xdnl.hazelcast.monitor.agent.producer;

import it.xdnl.hazelcast.monitor.agent.product.Product;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTopicProducer {
    private Set<TopicListener> listeners = new HashSet<>();
    private String topicType;

    public AbstractTopicProducer(String topicType) {
        this.topicType = topicType;
    }

    public void addListener(final TopicListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final TopicListener listener) {
        listeners.remove(listener);
    }

    public void removeListeners(final Collection<TopicListener> listenersToRemove) {
        for (TopicListener listener : listenersToRemove) {
            listeners.remove(listener);
        }
    }

    public void start() {
    }

    public void stop() {
    }

    public abstract Product produce();

    public void notice(final Product product) {
        for (TopicListener listener : listeners) {
            listener.notice(this, product);
        }
    }

    public String getTopicType() {
        return topicType;
    }

    public Set<TopicListener> getListeners() {
        return listeners;
    }
}
