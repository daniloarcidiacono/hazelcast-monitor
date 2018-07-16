package it.xdnl.hazelcast.monitor.agent.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.xdnl.hazelcast.monitor.agent.dto.request.GetClustersMessage;
import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeMessage;
import it.xdnl.hazelcast.monitor.agent.dto.response.TopicNoticeMessage;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "messageType",
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GetClustersMessage.class, name = TopicNoticeMessage.MESSAGE_TYPE),
    @JsonSubTypes.Type(value = SubscribeMessage.class, name = SubscribeMessage.MESSAGE_TYPE),
    @JsonSubTypes.Type(value = TopicNoticeMessage.class, name = TopicNoticeMessage.MESSAGE_TYPE),
    @JsonSubTypes.Type(value = ErrorMessage.class, name = ErrorMessage.MESSAGE_TYPE)
})
public abstract class Message {
    private String messageType;

    @JsonCreator
    public Message(@JsonProperty("messageType") final String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
