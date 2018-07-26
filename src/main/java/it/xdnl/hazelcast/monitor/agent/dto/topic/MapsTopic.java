package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.xdnl.hazelcast.monitor.agent.producer.MapsTopicProducer;

public class MapsTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = MapsTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public MapsTopic() {
        super(TOPIC_TYPE, null);
    }
}
