package it.xdnl.hazelcast.monitor.agent.exception;

import it.xdnl.hazelcast.monitor.agent.dto.request.UpdateSubscriptionRequest;
import it.xdnl.hazelcast.monitor.agent.producer.AbstractTopicProducer;

/**
 * Exception thrown when updating a subscription parameter fails.
 * @see UpdateSubscriptionRequest
 * @see AbstractTopicProducer#updateParameter(String, String)
 */
public class UpdateParameterException extends Exception {
    private String parameterName;
    private String actualValue;

    public UpdateParameterException() {
    }

    public UpdateParameterException(String message) {
        super(message);
    }

    public UpdateParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateParameterException(Throwable cause) {
        super(cause);
    }

    public UpdateParameterException(String message, Throwable cause, boolean b, boolean b1) {
        super(message, cause, b, b1);
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }
}
