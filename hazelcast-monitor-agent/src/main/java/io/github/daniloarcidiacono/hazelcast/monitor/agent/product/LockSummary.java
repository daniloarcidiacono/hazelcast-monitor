package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class LockSummary extends DistributedObjectSummary {
    private int lockCount;
    private long remainingLeaseTime;
    private boolean locked;

    public LockSummary(int lockCount, long remainingLeaseTime, boolean locked) {
        this.lockCount = lockCount;
        this.remainingLeaseTime = remainingLeaseTime;
        this.locked = locked;
    }

    public int getLockCount() {
        return lockCount;
    }

    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }

    public long getRemainingLeaseTime() {
        return remainingLeaseTime;
    }

    public void setRemainingLeaseTime(long remainingLeaseTime) {
        this.remainingLeaseTime = remainingLeaseTime;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}