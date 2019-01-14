package io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.response;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.Product;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;

@TypescriptDTO
public class SubscriptionNoticeResponse<T extends Product> extends AbstractMessage {
    public static final String MESSAGE_TYPE = "notice";
    private String topicType;
    private Long subscriptionId;
    private T notice;

    public SubscriptionNoticeResponse() {
        super(MESSAGE_TYPE);
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicName) {
        this.topicType = topicName;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Object getNotice() {
        return notice;
    }

    public void setNotice(T notice) {
        this.notice = notice;
    }
}
