package it.xdnl.hazelcast.monitor.agent.dto.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.agent.producer.FiltersTopicProducer;
import it.xdnl.hazelcast.monitor.agent.producer.InternalsTopicProducer;

@TypescriptDTO
public class FiltersTopic extends AbstractTopic {
    public static final String TOPIC_TYPE = FiltersTopicProducer.TOPIC_TYPE;

    @JsonCreator
    public FiltersTopic() {
        super(TOPIC_TYPE, null);
    }
}