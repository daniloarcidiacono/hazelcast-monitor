package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.producer.ClustersTopicProducer;

@TypescriptDTO
public class ClustersTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = ClustersTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public ClustersTopic() {
        super(TOPIC_TYPE, null);
    }
}
