package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.core.*;
import com.hazelcast.ringbuffer.Ringbuffer;
import it.xdnl.hazelcast.monitor.agent.product.StatisticsProduct;

public class StatisticsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "stats";
    private HazelcastInstance instance;

    public StatisticsTopicProducer(final String instanceName) {
        super(TOPIC_TYPE);
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public StatisticsProduct produce() {
        final StatisticsProduct product = new StatisticsProduct();

        int mapCount = 0;
        int queueCount = 0;
        int lockCount = 0;
        int topicCount = 0;
        int listCount = 0;
        int setCount = 0;
        int multiMapCount = 0;
        int ringbufferCount = 0;
        for (DistributedObject object : instance.getDistributedObjects()) {
            if (object instanceof IMap) {
                mapCount++;
            } else if (object instanceof IQueue) {
                queueCount++;
            } else if (object instanceof ILock) {
                lockCount++;
            } else if (object instanceof ITopic) {
                topicCount++;
            } else if (object instanceof IList) {
                listCount++;
            } else if (object instanceof ISet) {
                setCount++;
            } else if (object instanceof MultiMap) {
                multiMapCount++;
            } else if (object instanceof Ringbuffer) {
                ringbufferCount++;
            }
        }

        product.setMembersCount(instance.getCluster().getMembers().size());
        product.setMapCount(mapCount);
        product.setQueueCount(queueCount);
        product.setLockCount(lockCount);
        product.setTopicCount(topicCount);
        product.setListCount(listCount);
        product.setSetCount(setCount);
        product.setMultiMapCount(multiMapCount);
        product.setRingbufferCount(ringbufferCount);

        return product;
    }
}
