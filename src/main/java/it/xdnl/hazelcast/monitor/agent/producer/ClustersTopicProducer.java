package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import it.xdnl.hazelcast.monitor.agent.product.ClustersProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClustersTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "clusters";
    private static final Logger logger = LoggerFactory.getLogger(ClustersTopicProducer.class);

    public ClustersTopicProducer() {
        super(TOPIC_TYPE);
    }

    @Override
    public ClustersProduct produce() {
        final ClustersProduct product = new ClustersProduct();
        for (HazelcastInstance hazelcastInstance : Hazelcast.getAllHazelcastInstances()) {
            final String instanceName = hazelcastInstance.getConfig().getInstanceName();
            product.add(instanceName);
        }

        return product;
    }
}
