package it.xdnl.hazelcast.monitor.agent.dto.request;

import it.xdnl.hazelcast.monitor.agent.dto.Message;
import it.xdnl.hazelcast.monitor.agent.dto.request.topic.TopicMessage;

public class SubscribeMessage extends Message {
    public static final String MESSAGE_TYPE = "subscribe";
    private TopicMessage topic;

    public SubscribeMessage() {
        super(MESSAGE_TYPE);
    }

    public TopicMessage getTopic() {
        return topic;
    }

    public void setTopic(TopicMessage topic) {
        this.topic = topic;
    }
}
