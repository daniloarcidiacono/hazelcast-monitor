package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

@TypescriptDTO
public class AtomicReferenceSummary extends DistributedObjectSummary {
    private Object value;
    private String valueString;

    public AtomicReferenceSummary(Object value, String valueString) {
        this.value = value;
        this.valueString = valueString;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }
}
