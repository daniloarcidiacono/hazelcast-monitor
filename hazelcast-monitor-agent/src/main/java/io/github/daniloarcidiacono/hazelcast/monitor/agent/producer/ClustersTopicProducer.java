package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.Cluster;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.ClustersProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClustersTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "clusters";
    private static final Logger logger = LoggerFactory.getLogger(ClustersTopicProducer.class);

    public ClustersTopicProducer() {
        super(TOPIC_TYPE, null);
    }

    @Override
    public ClustersProduct produce() {
        final ClustersProduct product = new ClustersProduct();
        for (HazelcastInstance hazelcastInstance : Hazelcast.getAllHazelcastInstances()) {
            // @TODO: Clarify getName vs getInstanceName
//            final String instanceName = hazelcastInstance.getConfig().getInstanceName();
            final String instanceName = hazelcastInstance.getName();
            final String groupName = hazelcastInstance.getConfig().getGroupConfig().getName();

            product.add(new Cluster(instanceName, groupName));
        }

        return product;
    }
}
