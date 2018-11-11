package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.producer.InternalsTopicProducer;

@TypescriptDTO
public class InternalsTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = InternalsTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public InternalsTopic() {
        super(TOPIC_TYPE, null);
    }
}