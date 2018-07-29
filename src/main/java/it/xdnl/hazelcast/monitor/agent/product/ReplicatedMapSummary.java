package it.xdnl.hazelcast.monitor.agent.product;

import it.xdnl.chat.typescript.annotation.TypescriptDTO;

@TypescriptDTO
public class ReplicatedMapSummary extends DistributedObjectSummary {
    private int size;

    public ReplicatedMapSummary(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}