package it.xdnl.hazelcast.monitor.topic;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class StatisticsTopic extends Topic {
    public static final String TOPIC_TYPE = "stats";
    private HazelcastInstance instance;

    public StatisticsTopic(final String instanceName) {
        super(TOPIC_TYPE);
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }
}
