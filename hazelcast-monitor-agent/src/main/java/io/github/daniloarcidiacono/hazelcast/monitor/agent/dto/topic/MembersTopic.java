package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.producer.MembersTopicProducer;

@TypescriptDTO
public class MembersTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = MembersTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public MembersTopic(@JsonProperty("instanceName") final String instanceName) {
        super(TOPIC_TYPE, instanceName);
    }
}
