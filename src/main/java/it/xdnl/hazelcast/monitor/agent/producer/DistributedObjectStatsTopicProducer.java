package it.xdnl.hazelcast.monitor.agent.producer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import it.xdnl.hazelcast.monitor.agent.product.Product;
import it.xdnl.hazelcast.monitor.agent.product.StatsProduct;
import it.xdnl.hazelcast.monitor.agent.product.TopicStats;

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
            case TOPIC: {
                return produceTopicStats();
            }
        }

        return null;
    }

    private StatsProduct produceTopicStats() {
        final StatsProduct<TopicStats> product = new StatsProduct<>();
        product.setSampleTime(System.currentTimeMillis());

        try {
            final String __instanceName = instanceName;
            final String __objectName = objectName;
            final Map<Member, Future<TopicStats>> memberStats = executorService.submitToAllMembers(
                (Callable<TopicStats> & Serializable)() ->
                    TopicStats.fromHazelcast(
                        Hazelcast.getHazelcastInstanceByName(__instanceName)
                            .getTopic(__objectName)
                            .getLocalTopicStats()
                    )
            );

            for (Member member : memberStats.keySet()) {
                final Future<TopicStats> future = memberStats.get(member);
                final TopicStats stats = future.get();
                product.add(member.getAddress().toString(), stats);
            }

            product.setAggregated(TopicStats.aggregated(product.getMembers().values()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return product;
    }
}
