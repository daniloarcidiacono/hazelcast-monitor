package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class DistributedObjectsTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = "distributed_object";

    private DistributedObjectType distributedObjectType;

    @JsonCreator
    public DistributedObjectsTopic() {
        super(TOPIC_TYPE, null);
    }

    public DistributedObjectType getDistributedObjectType() {
        return distributedObjectType;
    }

    public void setDistributedObjectType(DistributedObjectType distributedObjectType) {
        this.distributedObjectType = distributedObjectType;
    }
}
