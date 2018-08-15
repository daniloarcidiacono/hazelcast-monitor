package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class DistributedObjectsProduct<T extends DistributedObjectSummary> implements Product {
    private List<T> objects = new ArrayList<>();

    public DistributedObjectsProduct() {
    }

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(final List<T> objects) {
        this.objects = objects;
    }
}
