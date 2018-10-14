package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "topicType",
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StatisticsTopic.class, name = StatisticsTopic.TOPIC_TYPE),
    @JsonSubTypes.Type(value = ClustersTopic.class, name = ClustersTopic.TOPIC_TYPE),
    @JsonSubTypes.Type(value = InternalsTopic.class, name = InternalsTopic.TOPIC_TYPE),
    @JsonSubTypes.Type(value = MembersTopic.class, name = MembersTopic.TOPIC_TYPE),
    @JsonSubTypes.Type(value = DistributedObjectsTopic.class, name = DistributedObjectsTopic.TOPIC_TYPE),
    @JsonSubTypes.Type(value = DistributedObjectTopic.class, name = DistributedObjectTopic.TOPIC_TYPE)
})
@TypescriptDTO
public abstract class AbstractTopic {
    private String topicType;
    private String instanceName;

    @JsonCreator
    public AbstractTopic(@JsonProperty("topicType") final String topicType,
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
