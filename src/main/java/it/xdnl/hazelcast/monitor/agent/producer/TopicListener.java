package it.xdnl.hazelcast.monitor.agent.producer;

import it.xdnl.hazelcast.monitor.agent.product.Product;

public interface TopicListener {
    void notice(final AbstractTopicProducer topic, final Product product);
}
