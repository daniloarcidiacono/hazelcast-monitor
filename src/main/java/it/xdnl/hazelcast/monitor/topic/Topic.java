package it.xdnl.hazelcast.monitor.topic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Topic {
    private Set<TopicListener> listeners = new HashSet<>();
    private String topicType;

    public Topic(String topicType) {
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

    public void notice(final Object object) {
        for (TopicListener listener : listeners) {
            listener.notice(this, object);
        }
    }

    public String getTopicType() {
        return topicType;
    }

    public Set<TopicListener> getListeners() {
        return listeners;
    }
}
