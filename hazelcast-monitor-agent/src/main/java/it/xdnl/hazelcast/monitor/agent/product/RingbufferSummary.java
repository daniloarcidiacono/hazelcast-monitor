package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class RingbufferSummary extends DistributedObjectSummary {
    private long size;
    private long capacity;
    private long remainingCapacity;
    private long headSequence;
    private long tailSequence;

    public RingbufferSummary(long size, long capacity, long remainingCapacity, long headSequence, long tailSequence) {
        this.size = size;
        this.capacity = capacity;
        this.remainingCapacity = remainingCapacity;
        this.headSequence = headSequence;
        this.tailSequence = tailSequence;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(long remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public long getHeadSequence() {
        return headSequence;
    }

    public void setHeadSequence(long headSequence) {
        this.headSequence = headSequence;
    }

    public long getTailSequence() {
        return tailSequence;
    }

    public void setTailSequence(long tailSequence) {
        this.tailSequence = tailSequence;
    }
}
