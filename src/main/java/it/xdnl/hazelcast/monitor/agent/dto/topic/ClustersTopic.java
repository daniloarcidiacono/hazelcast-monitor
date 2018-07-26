package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.xdnl.hazelcast.monitor.agent.producer.ClustersTopicProducer;

public class ClustersTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = ClustersTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public ClustersTopic() {
        super(TOPIC_TYPE, null);
    }
}
