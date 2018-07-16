package it.xdnl.hazelcast.monitor.agent.dto.request.topic;
import it.xdnl.hazelcast.monitor.topic.StatisticsTopic;

public class StatisticsTopicMessage extends TopicMessage {
    public static final String TOPIC_TYPE = StatisticsTopic.TOPIC_TYPE;

    public StatisticsTopicMessage(final String instanceName) {
        super(TOPIC_TYPE, instanceName);
    }
}
