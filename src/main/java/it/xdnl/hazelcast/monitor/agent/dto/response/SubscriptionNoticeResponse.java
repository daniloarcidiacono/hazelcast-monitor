package it.xdnl.hazelcast.monitor.agent.dto.response;

import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;

public class SubscriptionNoticeResponse extends AbstractMessage {
    public static final String MESSAGE_TYPE = "notice";
    private String topicType;
    private Long subscriptionId;
    private Object notice;

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

    public void setNotice(Object notice) {
        this.notice = notice;
    }
}
