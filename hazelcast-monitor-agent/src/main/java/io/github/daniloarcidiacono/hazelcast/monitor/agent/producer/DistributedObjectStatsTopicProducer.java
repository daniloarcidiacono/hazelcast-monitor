package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import com.hazelcast.core.*;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Producer that iterates on Hazelcast's distributed objects picking only type of {@code distributedObjectType}.
 * It returns the object statistics (both local and aggregation)
 */
public class DistributedObjectStatsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "distributed_object_stats";
    private static final Logger logger = LoggerFactory.getLogger(DistributedObjectStatsTopicProducer.class);

    private final DistributedObjectType distributedObjectType;
    private final String objectName;
    private final HazelcastInstance instance;
    private final IExecutorService executorService;

    public DistributedObjectStatsTopicProducer(final String instanceName,
                                               final DistributedObjectType distributedObjectType,
                                               final String objectName) {
        super(TOPIC_TYPE, instanceName);

        this.distributedObjectType = distributedObjectType;
        this.objectName = objectName;
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
        executorService = instance.getExecutorService("_hzMonitor_statsAggregator");
    }

    @Override
    public Product produce() {
        switch (distributedObjectType) {
            case EXECUTOR: {
                return produceExecutorStats();
            }

            case QUEUE: {
                return produceQueueStats();
            }

            case TOPIC: {
                return produceTopicStats();
            }

            case CACHE: {
                return produceCacheStats();
            }

            case MAP: {
                return produceMapStats();
            }

            case REPLICATEDMAP: {
                return produceReplicatedMapStats();
            }

            case MULTIMAP: {
                return produceMultiMapStats();
            }
        }

        return null;
    }

    static abstract class StatsTask<T> implements Callable<T>, Serializable, HazelcastInstanceAware {
        protected transient HazelcastInstance instance;
        protected final String objectName;

        public StatsTask(final String objectName) {
            this.objectName = objectName;
        }

        @Override
        public abstract T call() throws Exception;

        @Override
        public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
            instance = hazelcastInstance;
        }
    }

    static class MapStatsTask extends StatsTask<MapStats> {
        MapStatsTask(String objectName) {
            super(objectName);
        }

        @Override
        public MapStats call() {
            return MapStats.fromHazelcast(
                instance.getMap(objectName).getLocalMapStats()
            );
        }
    }

    private StatsProduct<MapStats> produceMapStats() {
        final StatsProduct<MapStats> product = produceStats(new MapStatsTask(objectName));
        product.setAggregated(MapStats.aggregated(product.getMembers().values()));
        return product;
    }

    static class ReplicatedMapStatsTask extends StatsTask<MapStats> {
        ReplicatedMapStatsTask(String objectName) {
            super(objectName);
        }

        @Override
        public MapStats call() {
            return MapStats.fromHazelcast(
                    instance.getReplicatedMap(objectName).getReplicatedMapStats()
            );
        }
    }

    private StatsProduct<MapStats> produceReplicatedMapStats() {
        final StatsProduct<MapStats> product = produceStats(new ReplicatedMapStatsTask(objectName));
        product.setAggregated(MapStats.aggregated(product.getMembers().values()));
        return product;
    }

    static class MultiMapStatsTask extends StatsTask<MapStats> {
        MultiMapStatsTask(String objectName) {
            super(objectName);
        }

        @Override
        public MapStats call() {
            return MapStats.fromHazelcast(
                    instance.getMultiMap(objectName).getLocalMultiMapStats()
            );
        }
    }

    private StatsProduct<MapStats> produceMultiMapStats() {
        final StatsProduct<MapStats> product = produceStats(new MultiMapStatsTask(objectName));
        product.setAggregated(MapStats.aggregated(product.getMembers().values()));
        return product;
    }

    static class CacheStatsTask extends StatsTask<CacheStats> {
        CacheStatsTask(String objectName) {
            super(objectName);
        }

        @Override
        public CacheStats call() {
            return CacheStats.fromHazelcast(
                instance.getCacheManager().getCache(objectName).getLocalCacheStatistics()
            );
        }
    }

    private StatsProduct<CacheStats> produceCacheStats() {
        final StatsProduct<CacheStats> product = produceStats(new CacheStatsTask(objectName));
        product.setAggregated(CacheStats.aggregated(product.getMembers().values()));
        return product;
    }

    static class ExecutorStatsTask extends StatsTask<ExecutorStats> {
        ExecutorStatsTask(String objectName) {
            super(objectName);
        }

        @Override
        public ExecutorStats call() {
            return ExecutorStats.fromHazelcast(
                    instance.getExecutorService(objectName).getLocalExecutorStats()
            );
        }
    }

    private StatsProduct<ExecutorStats> produceExecutorStats() {
        final StatsProduct<ExecutorStats> product = produceStats(new ExecutorStatsTask(objectName));
        product.setAggregated(ExecutorStats.aggregated(product.getMembers().values()));
        return product;
    }

    static class QueueStatsTask extends StatsTask<QueueStats> {
        QueueStatsTask(String objectName) {
            super(objectName);
        }

        @Override
        public QueueStats call() {
            return QueueStats.fromHazelcast(
                instance.getQueue(objectName).getLocalQueueStats()
            );
        }
    }

    private StatsProduct<QueueStats> produceQueueStats() {
        final StatsProduct<QueueStats> product = produceStats(new QueueStatsTask(objectName));
        product.setAggregated(QueueStats.aggregated(product.getMembers().values()));
        return product;
    }

    static class TopicStatsTask extends StatsTask<TopicStats> {
        TopicStatsTask(String objectName) {
            super(objectName);
        }

        @Override
        public TopicStats call() {
            return TopicStats.fromHazelcast(
                    instance.getTopic(objectName).getLocalTopicStats()
            );
        }
    }

    private StatsProduct<TopicStats> produceTopicStats() {
        final StatsProduct<TopicStats> product = produceStats(new TopicStatsTask(objectName));
        product.setAggregated(TopicStats.aggregated(product.getMembers().values()));
        return product;
    }

    private <T> StatsProduct<T> produceStats(final Callable<T> callable) {
        final StatsProduct<T> product = new StatsProduct<>();
        product.setSampleTime(System.currentTimeMillis());

        try {
            final Map<Member, Future<T>> memberStats = executorService.submitToAllMembers(callable);

            for (Member member : memberStats.keySet()) {
                final Future<T> future = memberStats.get(member);
                final T stats = future.get();
                product.add(member.getAddress().toString(), stats);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Could not produce statistics for {}", objectName, e);
        }

        return product;
    }
}
