package it.xdnl.hazelcast.monitor.agent.dto.request;

import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;

public class UnsubscribeRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "unsubscribe";
    private long subscriptionId;

    public UnsubscribeRequest() {
        super(MESSAGE_TYPE);
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
