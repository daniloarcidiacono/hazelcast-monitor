package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class CacheSummary extends DistributedObjectSummary {
    private int size;
    private boolean destroyed;

    public CacheSummary(int size, boolean destroyed) {
        this.size = size;
        this.destroyed = destroyed;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
