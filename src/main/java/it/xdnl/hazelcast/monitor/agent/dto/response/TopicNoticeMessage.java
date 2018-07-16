package it.xdnl.hazelcast.monitor.agent.dto.response;

import it.xdnl.hazelcast.monitor.agent.dto.Message;

public class TopicNoticeMessage extends Message {
    public static final String MESSAGE_TYPE = "notice";
    private String topicType;
    private Object notice;

    public TopicNoticeMessage() {
        super(MESSAGE_TYPE);
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicName) {
        this.topicType = topicName;
    }

    public Object getNotice() {
        return notice;
    }

    public void setNotice(Object notice) {
        this.notice = notice;
    }
}
