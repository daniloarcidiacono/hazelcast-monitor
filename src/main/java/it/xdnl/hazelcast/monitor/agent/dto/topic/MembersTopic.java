package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.xdnl.hazelcast.monitor.agent.producer.MembersTopicProducer;

public class MembersTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = MembersTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public MembersTopic(@JsonProperty("instanceName") final String instanceName) {
        super(TOPIC_TYPE, instanceName);
    }
}
