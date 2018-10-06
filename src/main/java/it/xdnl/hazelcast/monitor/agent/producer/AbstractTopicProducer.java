package it.xdnl.hazelcast.monitor.agent.producer;

import it.xdnl.hazelcast.monitor.agent.exception.UpdateParameterException;
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

    /**
     * Updates a parameter of this producer.
     * Subclasses may override this method to implement custom update logic.
     * Default implementation always throws an exception.
     *
     * @param parameter
     * @param value
     * @throws UpdateParameterException
     */
    public void updateParameter(final String parameter, final String value) throws UpdateParameterException {
        final UpdateParameterException updateParameterException = new UpdateParameterException("Unsupported parameter " + parameter);
        updateParameterException.setParameterName(parameter);
        updateParameterException.setActualValue(null);

        throw updateParameterException;
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
