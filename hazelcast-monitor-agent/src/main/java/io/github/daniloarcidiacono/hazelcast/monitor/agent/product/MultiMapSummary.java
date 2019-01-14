package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

@TypescriptDTO
public class MultiMapSummary extends DistributedObjectSummary {
    private int keyCount;
    private int valueCount;

    public MultiMapSummary(int valueCount, int keyCount) {
        this.keyCount = keyCount;
        this.valueCount = valueCount;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    public int getValueCount() {
        return valueCount;
    }

    public void setValueCount(int valueCount) {
        this.valueCount = valueCount;
    }
}