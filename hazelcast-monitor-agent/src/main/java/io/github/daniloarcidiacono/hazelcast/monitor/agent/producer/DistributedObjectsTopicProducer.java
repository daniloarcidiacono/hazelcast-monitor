package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import com.hazelcast.cache.ICache;
import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.core.*;
import com.hazelcast.ringbuffer.Ringbuffer;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.*;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic.DistributedObjectsTopic;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.exception.UpdateParameterException;

/**
 * Producer that iterates on Hazelcast's distributed objects picking only type of {@code distributedObjectType}.
 */
public class DistributedObjectsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = DistributedObjectsTopic.TOPIC_TYPE;
    private DistributedObjectType distributedObjectType;
    private HazelcastInstance instance;

    // Filtering
    private String filter;

    // Pagination
    private int page = 1;
    private int pageSize = 5;

    public DistributedObjectsTopicProducer(final String instanceName, final DistributedObjectType distributedObjectType) {
        super(TOPIC_TYPE);
        this.distributedObjectType = distributedObjectType;
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public void updateParameter(final String parameter, final String value) throws UpdateParameterException {
        switch (parameter) {
            case "filter":
                // Treat empty strings as "not filtering"
                filter = value.isEmpty() ? null : value.toLowerCase();
                break;

            case "page":
                try {
                    final int newPage = Integer.parseInt(value);
                    page = newPage;
                    if (page < 1) {
                        page = 1;
                    }
                } catch (NumberFormatException e) {
                    final UpdateParameterException updateParameterException = new UpdateParameterException("Invalid page value: " + value);
                    updateParameterException.setParameterName(parameter);
                    updateParameterException.setActualValue(String.valueOf(page));

                    throw updateParameterException;
                }

                break;

            case "pageSize":
                final int newPageSize = Integer.parseInt(value);
                try {
                    pageSize = newPageSize;
                    if (pageSize < 1) {
                        pageSize = 1;
                    }
                } catch (NumberFormatException e) {
                    final UpdateParameterException updateParameterException = new UpdateParameterException("Invalid page size value: " + value);
                    updateParameterException.setParameterName(parameter);
                    updateParameterException.setActualValue(String.valueOf(pageSize));

                    throw updateParameterException;
                }

                break;

            default:
                super.updateParameter(parameter, value);
                break;
        }
    }

    @Override
    public DistributedObjectsProduct<DistributedObjectSummary> produce() {
        final DistributedObjectsProduct<DistributedObjectSummary> product = new DistributedObjectsProduct<>();

        // Calculate pagination boundaries
        final int start = pageSize * (page - 1);
        final int end = start + pageSize - 1;

        // Start from the beginning
        int current = 0;

        // For each distributed object
        for (DistributedObject object : instance.getDistributedObjects()) {
            // If its of the type we are searching for...
            if (distributedObjectType.matches(object)) {
                // Filter by name
                if (filter != null && !object.getName().toLowerCase().contains(filter)) {
                    continue;
                }

                // Pick the element only if it is contained in the page
                if (current >= start && current <= end) {
                    final DistributedObjectSummary summary = produceInternal(object);
                    summary.setName(object.getName());
                    summary.setPartitionKey(object.getPartitionKey());
                    product.getObjects().add(summary);
                }

                // If we have filled the page, stop
                if (current >= end) {
                    break;
                }

                // We had a match
                current++;
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

            case CARDINALITYESTIMATOR: {
                final CardinalityEstimator casted = (CardinalityEstimator)object;
                return new CardinalityEstimatorSummary(casted.estimate());
            }

            case EXECUTOR: {
                final IExecutorService casted = (IExecutorService)object;
                return new ExecutorSummary();
            }
        }

        return null;
    }
}
