package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class SemaphoreSummary extends DistributedObjectSummary {
    private int permits;

    public SemaphoreSummary(int permits) {
        this.permits = permits;
    }

    public int getPermits() {
        return permits;
    }

    public void setPermits(int permits) {
        this.permits = permits;
    }
}
