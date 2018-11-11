package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.*;
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
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(DistributedObjectStatsTopicProducer.class);

    private final DistributedObjectType distributedObjectType;
    private final String objectName;
    private final String instanceName;
    private final HazelcastInstance instance;
    private final IExecutorService executorService;

    static {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public DistributedObjectStatsTopicProducer(final String instanceName,
                                               final DistributedObjectType distributedObjectType,
                                               final String objectName) {
        super(TOPIC_TYPE);

        this.instanceName = instanceName;
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

    private StatsProduct<MapStats> produceMapStats() {
        final String __instanceName = instanceName;
        final String __objectName = objectName;
        final StatsProduct<MapStats> product = produceStats((Callable<MapStats> & Serializable)() ->
            MapStats.fromHazelcast(
                Hazelcast.getHazelcastInstanceByName(__instanceName)
                    .getMap(__objectName)
                    .getLocalMapStats()
            )
        );

        product.setAggregated(MapStats.aggregated(product.getMembers().values()));
        return product;
    }

    private StatsProduct<MapStats> produceReplicatedMapStats() {
        final String __instanceName = instanceName;
        final String __objectName = objectName;
        final StatsProduct<MapStats> product = produceStats((Callable<MapStats> & Serializable)() ->
            MapStats.fromHazelcast(
                Hazelcast.getHazelcastInstanceByName(__instanceName)
                    .getReplicatedMap(__objectName)
                    .getReplicatedMapStats()
            )
        );

        product.setAggregated(MapStats.aggregated(product.getMembers().values()));
        return product;
    }

    private StatsProduct<MapStats> produceMultiMapStats() {
        final String __instanceName = instanceName;
        final String __objectName = objectName;
        final StatsProduct<MapStats> product = produceStats((Callable<MapStats> & Serializable)() ->
            MapStats.fromHazelcast(
                Hazelcast.getHazelcastInstanceByName(__instanceName)
                    .getMultiMap(__objectName)
                    .getLocalMultiMapStats()
            )
        );

        product.setAggregated(MapStats.aggregated(product.getMembers().values()));
        return product;
    }

    private StatsProduct<CacheStats> produceCacheStats() {
        final String __instanceName = instanceName;
        final String __objectName = objectName;
        final StatsProduct<CacheStats> product = produceStats((Callable<CacheStats> & Serializable)() ->
            CacheStats.fromHazelcast(
                Hazelcast.getHazelcastInstanceByName(__instanceName)
                    .getCacheManager().getCache(__objectName)
                    .getLocalCacheStatistics()
            )
        );

        product.setAggregated(CacheStats.aggregated(product.getMembers().values()));
        return product;
    }

    private StatsProduct<ExecutorStats> produceExecutorStats() {
        final String __instanceName = instanceName;
        final String __objectName = objectName;
        final StatsProduct<ExecutorStats> product = produceStats((Callable<ExecutorStats> & Serializable)() ->
            ExecutorStats.fromHazelcast(
                Hazelcast.getHazelcastInstanceByName(__instanceName)
                    .getExecutorService(__objectName)
                    .getLocalExecutorStats()
                )
        );

        product.setAggregated(ExecutorStats.aggregated(product.getMembers().values()));
        return product;
    }

    private StatsProduct<QueueStats> produceQueueStats() {
        final String __instanceName = instanceName;
        final String __objectName = objectName;
        final StatsProduct<QueueStats> product = produceStats((Callable<QueueStats> & Serializable)() ->
            QueueStats.fromHazelcast(
                Hazelcast.getHazelcastInstanceByName(__instanceName)
                    .getQueue(__objectName)
                    .getLocalQueueStats()
            )
        );

        product.setAggregated(QueueStats.aggregated(product.getMembers().values()));
        return product;
    }

    private StatsProduct<TopicStats> produceTopicStats() {
        final String __instanceName = instanceName;
        final String __objectName = objectName;
        final StatsProduct<TopicStats> product = produceStats((Callable<TopicStats> & Serializable)() ->
            TopicStats.fromHazelcast(
                Hazelcast.getHazelcastInstanceByName(__instanceName)
                    .getTopic(__objectName)
                    .getLocalTopicStats()
            )
        );

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
