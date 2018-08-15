package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class TopicProduct implements Product {
    private MembersProduct.MemberSummary publisher;
    private Object message;
    private String messageString;
    private long publishTime;

    public TopicProduct() {
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public MembersProduct.MemberSummary getPublisher() {
        return publisher;
    }

    public void setPublisher(MembersProduct.MemberSummary publisher) {
        this.publisher = publisher;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
