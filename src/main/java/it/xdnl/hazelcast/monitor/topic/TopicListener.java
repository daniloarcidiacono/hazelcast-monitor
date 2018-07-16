package it.xdnl.hazelcast.monitor.topic;

public interface TopicListener {
    void notice(final Topic topic, final Object object);
}
