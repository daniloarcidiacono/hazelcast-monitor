package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class MapSummary extends DistributedObjectSummary {
    private int size;

    public MapSummary(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}