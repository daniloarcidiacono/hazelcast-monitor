package it.xdnl.hazelcast.monitor.agent.producer;

public interface TopicListener {
    void notice(final AbstractTopicProducer topic, final Object object);
}
