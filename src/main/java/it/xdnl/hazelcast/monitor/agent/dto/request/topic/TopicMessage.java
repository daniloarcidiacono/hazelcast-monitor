package it.xdnl.hazelcast.monitor.agent.dto.request.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "topicType",
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StatisticsTopicMessage.class, name = StatisticsTopicMessage.TOPIC_TYPE)
})
public abstract class TopicMessage {
    private String topicType;
    private String instanceName;

    @JsonCreator
    public TopicMessage(@JsonProperty("topicType") final String topicType,
                        @JsonProperty("instanceName") final String instanceName) {
        this.topicType = topicType;
        this.instanceName = instanceName;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
