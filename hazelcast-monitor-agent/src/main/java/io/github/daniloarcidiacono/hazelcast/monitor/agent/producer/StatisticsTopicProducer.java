package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import com.hazelcast.cache.ICache;
import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.core.*;
import com.hazelcast.memory.DefaultMemoryStats;
import com.hazelcast.monitor.LocalMemoryStats;
import com.hazelcast.monitor.impl.LocalMemoryStatsImpl;
import com.hazelcast.ringbuffer.Ringbuffer;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.StatisticsProduct;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.ExecutorService;

public class StatisticsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "stats";
    private final LocalMemoryStats stats = new LocalMemoryStatsImpl(new DefaultMemoryStats());
    private final OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
    private HazelcastInstance instance;

    public StatisticsTopicProducer(final String instanceName) {
        super(TOPIC_TYPE);
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public StatisticsProduct produce() {
        final StatisticsProduct product = new StatisticsProduct();

        int atomicLongCount = 0;
        int atomicReferenceCount = 0;
        int cacheCount = 0;
        int cardinalityEstimatorsCount = 0;
        int countDownLatchCount = 0;
        int executorsCount = 0;
        int listCount = 0;
        int lockCount = 0;
        int mapCount = 0;
        int multiMapCount = 0;
        int queueCount = 0;
        int replicatedMapCount = 0;
        int ringbufferCount = 0;
        int semaphoreCount = 0;
        int setCount = 0;
        int topicCount = 0;

        for (DistributedObject object : instance.getDistributedObjects()) {
            if (object instanceof IAtomicLong) {
                atomicLongCount++;
            } else if (object instanceof IAtomicReference) {
                atomicReferenceCount++;
            } else if (object instanceof ICache) {
                cacheCount++;
            } else if (object instanceof CardinalityEstimator) {
                cardinalityEstimatorsCount++;
            } else if (object instanceof ICountDownLatch) {
                countDownLatchCount++;
            } else if (object instanceof ExecutorService) {
                executorsCount++;
            } else if (object instanceof IList) {
                listCount++;
            } else if (object instanceof ILock) {
                lockCount++;
            } else if (object instanceof IMap) {
                mapCount++;
            } else if (object instanceof MultiMap) {
                multiMapCount++;
            } else if (object instanceof IQueue) {
                queueCount++;
            } else if (object instanceof ReplicatedMap) {
                replicatedMapCount++;
            } else if (object instanceof Ringbuffer) {
                ringbufferCount++;
            } else if (object instanceof ISemaphore) {
                semaphoreCount++;
            } else if (object instanceof ISet) {
                setCount++;
            } else if (object instanceof ITopic) {
                topicCount++;
            }
        }

        product.setMembersCount(instance.getCluster().getMembers().size());
        product.setAtomicLongCount(atomicLongCount);
        product.setAtomicReferenceCount(atomicReferenceCount);
        product.setCacheCount(cacheCount);
        product.setCardinalityEstimatorsCount(cardinalityEstimatorsCount);
        product.setCountDownLatchCount(countDownLatchCount);
        product.setExecutorsCount(executorsCount);
        product.setListCount(listCount);
        product.setLockCount(lockCount);
        product.setMapCount(mapCount);
        product.setMultiMapCount(multiMapCount);
        product.setQueueCount(queueCount);
        product.setReplicatedMapCount(replicatedMapCount);
        product.setRingbufferCount(ringbufferCount);
        product.setSemaphoreCount(semaphoreCount);
        product.setSetCount(setCount);
        product.setTopicCount(topicCount);

        // https://stackoverflow.com/questions/47177/how-do-i-monitor-the-computers-cpu-memory-and-disk-usage-in-java
        if (mxBean instanceof com.sun.management.OperatingSystemMXBean) {
            final com.sun.management.OperatingSystemMXBean sunMxBean = (com.sun.management.OperatingSystemMXBean)mxBean;
            product.setSystemCpuLoad(sunMxBean.getSystemCpuLoad());
            product.setProcessCpuLoad(sunMxBean.getProcessCpuLoad());
            product.setTotalPhysicalMemory(sunMxBean.getTotalPhysicalMemorySize());
            product.setFreePhysicalMemory(sunMxBean.getFreePhysicalMemorySize());
        } else {
            product.setSystemCpuLoad(-1);
            product.setProcessCpuLoad(-1);
            product.setTotalPhysicalMemory(stats.getTotalPhysical());
            product.setFreePhysicalMemory(stats.getFreePhysical());
        }

        product.setMaxHeapMemory(stats.getMaxHeap());
        product.setUsedHeapMemory(stats.getUsedHeap());
        product.setMaxNativeMemory(stats.getMaxNative());
        product.setFreeNativeMemory(stats.getFreeNative());

        return product;
    }
}
