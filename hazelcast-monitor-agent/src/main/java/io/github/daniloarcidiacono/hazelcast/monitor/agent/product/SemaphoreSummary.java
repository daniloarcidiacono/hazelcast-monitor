package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

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
