package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;

@TypescriptDTO
public class PullSubscriptionRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "pull_subscription";
    private long subscriptionId;

    public PullSubscriptionRequest() {
        super(MESSAGE_TYPE);
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
