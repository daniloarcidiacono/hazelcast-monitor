package it.xdnl.hazelcast.monitor.agent.product;

import it.xdnl.chat.typescript.annotation.TypescriptDTO;

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