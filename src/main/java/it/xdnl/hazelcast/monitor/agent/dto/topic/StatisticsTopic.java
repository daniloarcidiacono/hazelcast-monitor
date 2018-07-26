package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.xdnl.hazelcast.monitor.agent.producer.StatisticsTopicProducer;

public class StatisticsTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = StatisticsTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public StatisticsTopic(@JsonProperty("instanceName") final String instanceName) {
        super(TOPIC_TYPE, instanceName);
    }
}
