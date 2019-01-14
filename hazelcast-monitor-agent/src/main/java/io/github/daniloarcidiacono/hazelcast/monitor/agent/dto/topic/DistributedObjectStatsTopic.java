package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.producer.DistributedObjectStatsTopicProducer;

/**
 * Topic for a specific distributed object
 */
@TypescriptDTO
public class DistributedObjectStatsTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = DistributedObjectStatsTopicProducer.TOPIC_TYPE;
    private DistributedObjectType distributedObjectType;
    private String objectName;

    @JsonCreator
    public DistributedObjectStatsTopic(@JsonProperty("instanceName") final String instanceName, @JsonProperty("objectName") final String objectName) {
        super(TOPIC_TYPE, instanceName);
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public DistributedObjectType getDistributedObjectType() {
        return distributedObjectType;
    }

    public void setDistributedObjectType(DistributedObjectType distributedObjectType) {
        this.distributedObjectType = distributedObjectType;
    }
}
