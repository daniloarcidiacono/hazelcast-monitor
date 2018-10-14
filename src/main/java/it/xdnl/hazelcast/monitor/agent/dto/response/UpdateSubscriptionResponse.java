package it.xdnl.hazelcast.monitor.agent.dto.response;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;

@TypescriptDTO
public class UpdateSubscriptionResponse extends AbstractMessage {
    public static final String MESSAGE_TYPE = "update_subscription_response";
    private Long subscriptionId;
    private String parameter;
    private String value;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
