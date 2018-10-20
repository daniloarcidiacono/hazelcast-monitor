package it.xdnl.hazelcast.monitor.agent.dto.request;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;

import java.util.Map;

@TypescriptDTO
public class UpdateSubscriptionRequest extends AbstractMessage {
    public static final String MESSAGE_TYPE = "update_subscription";
    private long subscriptionId;

    @TypescriptField(required = false)
    private Map<String, String> parameters;

    public UpdateSubscriptionRequest() {
        super(MESSAGE_TYPE);
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
