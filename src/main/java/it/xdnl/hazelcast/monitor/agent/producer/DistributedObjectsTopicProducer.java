package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.*;
import com.hazelcast.ringbuffer.Ringbuffer;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectsTopic;
import it.xdnl.hazelcast.monitor.agent.product.*;

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

            case QUEUE: {
                final IQueue casted = (IQueue)object;
                return new QueueSummary(casted.size());
            }

            case REPLICATEDMAP: {
                final ReplicatedMap casted = (ReplicatedMap) object;
                return new ReplicatedMapSummary(casted.size());
            }

            case SET: {
                final ISet casted = (ISet)object;
                return new SetSummary(casted.size());
            }

            case TOPIC: {
                final ITopic casted = (ITopic)object;
                return new TopicSummary();
            }

            case ATOMICLONG: {
                final IAtomicLong casted = (IAtomicLong)object;
                return new AtomicLongSummary(casted.get());
            }

            case ATOMICREFERENCE: {
                final IAtomicReference casted = (IAtomicReference)object;
                final Object reference = casted.get();
                return new AtomicReferenceSummary(reference, reference != null ? reference.toString() : "(null)");
            }

            case COUNTDOWNLATCH: {
                final ICountDownLatch casted = (ICountDownLatch)object;
                return new CountDownLatchSummary(casted.getCount());
            }

            case RINGBUFFER: {
                final Ringbuffer casted = (Ringbuffer)object;
                return new RingbufferSummary(casted.size(), casted.capacity(), casted.remainingCapacity(), casted.headSequence(), casted.tailSequence());
            }

            case SEMAPHORE: {
                final ISemaphore casted = (ISemaphore)object;
                return new SemaphoreSummary(casted.availablePermits());
            }

            case CACHE: {
                final ICache casted = (ICache)object;
                return new CacheSummary(casted.size(), casted.isDestroyed());
            }
        }

        return null;
    }
}
