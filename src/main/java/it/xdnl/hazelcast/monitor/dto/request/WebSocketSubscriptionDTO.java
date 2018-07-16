package it.xdnl.hazelcast.monitor.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.xdnl.chat.typescript.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.dto.WebSocketDTO;

/**
 * Subscription request.
 */
@TypescriptDTO
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subType", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(value = {
    @JsonSubTypes.Type(value = WebSocketSubscriptionGenericStatsDTO.class, name = WebSocketSubscriptionGenericStatsDTO.SUB_DTO_TYPE)
})
public abstract class WebSocketSubscriptionDTO extends WebSocketDTO {
    public static final String DTO_TYPE = "subscribe";
    private String subType;

    // Needed for polymorphic deserialization
    public WebSocketSubscriptionDTO() {
        super(DTO_TYPE);
        this.subType = null;
    }

    public WebSocketSubscriptionDTO(String subType) {
        super(DTO_TYPE);
        this.subType = subType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    @Override
    public String toString() {
        return "WebSocketSubscriptionDTO{" +
                "dtoType='" + getDtoType() + '\'' +
                "subType='" + subType + '\'' +
                '}';
    }
}
