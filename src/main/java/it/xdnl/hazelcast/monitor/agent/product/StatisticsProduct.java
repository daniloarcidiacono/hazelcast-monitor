package it.xdnl.hazelcast.monitor.agent.product;

public class StatisticsProduct implements Product {
    private int membersCount;
    private int atomicLongCount;
    private int atomicReferenceCount;
    private int cacheCount;
    private int countDownLatchCount;
    private int listCount;
    private int lockCount;
    private int mapCount;
    private int multiMapCount;
    private int queueCount;
    private int replicatedMapCount;
    private int ringbufferCount;
    private int semaphoreCount;
    private int setCount;
    private int topicCount;

    public StatisticsProduct() {
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public int getAtomicLongCount() {
        return atomicLongCount;
    }

    public void setAtomicLongCount(int atomicLongCount) {
        this.atomicLongCount = atomicLongCount;
    }

    public int getAtomicReferenceCount() {
        return atomicReferenceCount;
    }

    public void setAtomicReferenceCount(int atomicReferenceCount) {
        this.atomicReferenceCount = atomicReferenceCount;
    }

    public int getCacheCount() {
        return cacheCount;
    }

    public void setCacheCount(int cacheCount) {
        this.cacheCount = cacheCount;
    }

    public int getCountDownLatchCount() {
        return countDownLatchCount;
    }

    public void setCountDownLatchCount(int countDownLatchCount) {
        this.countDownLatchCount = countDownLatchCount;
    }

    public int getListCount() {
        return listCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public int getLockCount() {
        return lockCount;
    }

    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }

    public int getMapCount() {
        return mapCount;
    }

    public void setMapCount(int mapCount) {
        this.mapCount = mapCount;
    }

    public int getMultiMapCount() {
        return multiMapCount;
    }

    public void setMultiMapCount(int multiMapCount) {
        this.multiMapCount = multiMapCount;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
    }

    public int getReplicatedMapCount() {
        return replicatedMapCount;
    }

    public void setReplicatedMapCount(int replicatedMapCount) {
        this.replicatedMapCount = replicatedMapCount;
    }

    public int getRingbufferCount() {
        return ringbufferCount;
    }

    public void setRingbufferCount(int ringbufferCount) {
        this.ringbufferCount = ringbufferCount;
    }

    public int getSemaphoreCount() {
        return semaphoreCount;
    }

    public void setSemaphoreCount(int semaphoreCount) {
        this.semaphoreCount = semaphoreCount;
    }

    public int getSetCount() {
        return setCount;
    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }
}
