package it.xdnl.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.dto.topic.AbstractTopic;

import java.util.Map;

@TypescriptDTO
public class SubscribeRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "subscribe";
    private long frequency;
    private AbstractTopic topic;

    @TypescriptField(required = false)
    private Map<String, String> parameters;

    public SubscribeRequest() {
        super(MESSAGE_TYPE);
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public AbstractTopic getTopic() {
        return topic;
    }

    public void setTopic(AbstractTopic topic) {
        this.topic = topic;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
