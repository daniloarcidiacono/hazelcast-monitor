package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.*;
import com.hazelcast.ringbuffer.Ringbuffer;

public enum DistributedObjectType {
    ATOMICLONG(IAtomicLong.class),
    ATOMICREFERENCE(IAtomicReference.class),
    CACHE(ICache.class),
    COUNTDOWNLATCH(ICountDownLatch.class),
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
