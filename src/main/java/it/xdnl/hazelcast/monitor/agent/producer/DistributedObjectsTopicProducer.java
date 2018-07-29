package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.core.*;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectsTopic;
import it.xdnl.hazelcast.monitor.agent.product.*;

import java.awt.*;

/**
 * Producer that iterates on Hazelcast's distributed objects picking only type of {@code distributedObjectType}.
 */
public class DistributedObjectsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = DistributedObjectsTopic.TOPIC_TYPE;
    private DistributedObjectType distributedObjectType;
    private HazelcastInstance instance;

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
                final ILock casted = (ILock)object;
                return new LockSummary(casted.getLockCount(), casted.getRemainingLeaseTime(), casted.isLocked());
            }

            case LIST: {
                final IList casted = (IList)object;
                return new ListSummary(casted.size());
            }

            case MULTIMAP: {
                final MultiMap casted = (MultiMap)object;
                return new MultiMapSummary(casted.size(), casted.keySet().size());
            }
        }

        return null;
    }
}
