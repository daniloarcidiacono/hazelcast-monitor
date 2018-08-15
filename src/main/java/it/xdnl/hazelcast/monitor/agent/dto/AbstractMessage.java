package it.xdnl.hazelcast.monitor.agent.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField;
import it.xdnl.hazelcast.monitor.agent.dto.request.SubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.request.UnsubscribeRequest;
import it.xdnl.hazelcast.monitor.agent.dto.response.SubscriptionNoticeResponse;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "messageType",
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SubscribeRequest.class, name = SubscribeRequest.MESSAGE_TYPE),
    @JsonSubTypes.Type(value = UnsubscribeRequest.class, name = UnsubscribeRequest.MESSAGE_TYPE),
    @JsonSubTypes.Type(value = SubscriptionNoticeResponse.class, name = SubscriptionNoticeResponse.MESSAGE_TYPE),
    @JsonSubTypes.Type(value = ErrorMessage.class, name = ErrorMessage.MESSAGE_TYPE)
})
@TypescriptDTO
public abstract class AbstractMessage {
    private String messageType;
    
    @TypescriptField(required = false)
    private Long messageId;

    @JsonCreator
    public AbstractMessage(@JsonProperty("messageType") final String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
