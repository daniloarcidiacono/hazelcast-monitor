package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.exception.UpdateParameterException;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.Product;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTopicProducer {
    private Set<TopicListener> listeners = new HashSet<>();

    private final String topicType;

    /**
     * The name of the Hazelcast instance to which the producer is bound, or null if
     * the producer is not specific for any instance (e.g. {@link ClustersTopicProducer}).
     */
    protected final String instanceName;

    public AbstractTopicProducer(final String topicType, final String instanceName) {
        this.topicType = topicType;
        this.instanceName = instanceName;
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
     * Implementations should check if the new value is the same as the current one, and do nothing in that case.
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

    public String getInstanceName() {
        return instanceName;
    }

    public Set<TopicListener> getListeners() {
        return listeners;
    }
}
