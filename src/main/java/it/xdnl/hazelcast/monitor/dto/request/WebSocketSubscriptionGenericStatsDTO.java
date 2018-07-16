package it.xdnl.hazelcast.monitor.dto.request;

import it.xdnl.chat.typescript.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.dto.WebSocketDTO;

/**
 * Subscription request for generic statistics.
 */
@TypescriptDTO
public class WebSocketSubscriptionGenericStatsDTO extends WebSocketSubscriptionDTO {
    public static final String SUB_DTO_TYPE = "stats";

    public WebSocketSubscriptionGenericStatsDTO() {
        super(SUB_DTO_TYPE);
    }
}
