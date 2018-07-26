package it.xdnl.hazelcast.monitor.agent.dto.request;

import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.dto.topic.AbstractTopic;

public class SubscribeRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "subscribe";
    private long frequency;
    private AbstractTopic topic;

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
}
