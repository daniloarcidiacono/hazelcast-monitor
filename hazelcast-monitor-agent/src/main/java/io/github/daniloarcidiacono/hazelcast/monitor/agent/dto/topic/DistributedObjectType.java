package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic;

import com.hazelcast.cache.ICache;
import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.core.*;
import com.hazelcast.ringbuffer.Ringbuffer;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

@TypescriptDTO
public enum DistributedObjectType {
    ATOMICLONG(IAtomicLong.class),
    ATOMICREFERENCE(IAtomicReference.class),
    CACHE(ICache.class),
    CARDINALITYESTIMATOR(CardinalityEstimator.class),
    COUNTDOWNLATCH(ICountDownLatch.class),
    EXECUTOR(IExecutorService.class),
    LIST(IList.class),
    LOCK(ILock.class),
    MAP(IMap.class),
    MULTIMAP(MultiMap.class),
    QUEUE(IQueue.class),
    REPLICATEDMAP(ReplicatedMap.class),
    RINGBUFFER(Ringbuffer.class),
    SEMAPHORE(ISemaphore.class),
    SET(ISet.class),
    TOPIC(ITopic.class);

    private Class hazelclass;

    DistributedObjectType(final Class hazelclass) {
        this.hazelclass = hazelclass;
    }

    public boolean matches(final DistributedObject object) {
        return hazelclass.isAssignableFrom(object.getClass());
    }
}
