package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.xdnl.hazelcast.monitor.agent.producer.MapTopicProducer;
import it.xdnl.hazelcast.monitor.agent.producer.StatisticsTopicProducer;

public class MapTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = MapTopicProducer.TOPIC_TYPE;

    private String mapName;

    @JsonCreator
    public MapTopic(@JsonProperty("instanceName") final String instanceName, @JsonProperty("mapName") final String mapName) {
        super(TOPIC_TYPE, instanceName);
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
