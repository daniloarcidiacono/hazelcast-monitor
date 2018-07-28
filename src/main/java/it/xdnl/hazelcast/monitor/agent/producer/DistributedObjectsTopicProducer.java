package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.core.*;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectsTopic;
import it.xdnl.hazelcast.monitor.agent.product.DistributedObjectSummary;
import it.xdnl.hazelcast.monitor.agent.product.DistributedObjectsProduct;
import it.xdnl.hazelcast.monitor.agent.product.MapSummary;

/**
 * Producer that iterates on Hazelcast's distributed objects picking only type of {@code distributedObjectType}.
 */
public class DistributedObjectsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = DistributedObjectsTopic.TOPIC_TYPE;
    private DistributedObjectType distributedObjectType;
    protected HazelcastInstance instance;

    public DistributedObjectsTopicProducer(final String instanceName, final DistributedObjectType distributedObjectType) {
        super(TOPIC_TYPE);
        this.distributedObjectType = distributedObjectType;
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public DistributedObjectsProduct<DistributedObjectSummary> produce() {
        final DistributedObjectsProduct<DistributedObjectSummary> product = new DistributedObjectsProduct<>();
        for (DistributedObject object : instance.getDistributedObjects()) {
            if (distributedObjectType.matches(object)) {
                final DistributedObjectSummary summary = produceInternal(object);
                summary.setName(object.getName());
                summary.setPartitionKey(object.getPartitionKey());
                product.getObjects().add(summary);
            }
        }

        return product;
    }

    private DistributedObjectSummary produceInternal(final DistributedObject object) {
        switch (distributedObjectType) {
            case MAP: {
                final IMap casted = (IMap)object;
                return new MapSummary(casted.size());
            }

            case LOCK: {
                final ILock casted = (ILock) object;
//                return casted.ge;
            }
        }

        return null;
    }
}
