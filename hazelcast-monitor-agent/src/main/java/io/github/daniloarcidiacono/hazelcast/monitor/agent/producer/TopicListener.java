package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.Product;

public interface TopicListener {
    void notice(final AbstractTopicProducer topic, final Product product);
}
