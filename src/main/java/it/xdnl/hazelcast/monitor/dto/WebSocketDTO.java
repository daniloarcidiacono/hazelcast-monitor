package it.xdnl.hazelcast.monitor.dto;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.xdnl.chat.typescript.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.dto.request.WebSocketSubscriptionDTO;

/**
 * Base DTO of both messages sent and received.
 *
 * @author Danilo Arcidiacono (danilo.arcidiacono@gmail.com)
 */
@TypescriptDTO
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "dtoType", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(value = {
    @JsonSubTypes.Type(value = WebSocketSubscriptionDTO.class, name = WebSocketSubscriptionDTO.DTO_TYPE)
})
public abstract class WebSocketDTO {
    private String dtoType;

    public WebSocketDTO(String dtoType) {
        assert dtoType != null;
        this.dtoType = dtoType;
    }

    public String getDtoType() {
        return dtoType;
    }

    public void setDtoType(final String dtoType) {
        this.dtoType = dtoType;
    }

    @Override
    public String toString() {
        return "WebSocketDTO{" +
                "dtoType='" + dtoType + '\'' +
                '}';
    }
}
