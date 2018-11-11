package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;

import java.util.Map;

@TypescriptDTO
public class UpdateSubscriptionResponse extends AbstractMessage {
    public static final String MESSAGE_TYPE = "update_subscription_response";
    private Long subscriptionId;

    @TypescriptField
    private Map<String, String> parameters;

    @TypescriptField(required = false)
    private String error;

    public UpdateSubscriptionResponse() {
        super(MESSAGE_TYPE);
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
