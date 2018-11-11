package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class ListSummary extends DistributedObjectSummary {
    private int size;

    public ListSummary(int size)  {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}