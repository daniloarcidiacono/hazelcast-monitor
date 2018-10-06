package it.xdnl.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;

@TypescriptDTO
public class UpdateSubscriptionRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "update_subscription";
    private long subscriptionId;
    private String parameter;
    private String value;

    public UpdateSubscriptionRequest() {
        super(MESSAGE_TYPE);
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
