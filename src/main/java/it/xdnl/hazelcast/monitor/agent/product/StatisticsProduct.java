package it.xdnl.hazelcast.monitor.agent.product;

public class StatisticsProduct implements Product {
    private int membersCount = 0;
    private int mapCount;
    private int queueCount;
    private int lockCount;
    private int topicCount;
    private int listCount;
    private int setCount;
    private int multiMapCount;
    private int ringbufferCount;

    public StatisticsProduct() {
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMapCount(int mapCount) {
        this.mapCount = mapCount;
    }

    public int getMapCount() {
        return mapCount;
    }

    public void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }

    public int getLockCount() {
        return lockCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public int getListCount() {
        return listCount;
    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }

    public int getSetCount() {
        return setCount;
    }

    public void setMultiMapCount(int multiMapCount) {
        this.multiMapCount = multiMapCount;
    }

    public int getMultiMapCount() {
        return multiMapCount;
    }

    public void setRingbufferCount(int ringbufferCount) {
        this.ringbufferCount = ringbufferCount;
    }

    public int getRingbufferCount() {
        return ringbufferCount;
    }
}
