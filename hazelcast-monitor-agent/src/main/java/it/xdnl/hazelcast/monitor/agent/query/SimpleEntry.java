package it.xdnl.hazelcast.monitor.agent.query;

import java.util.Map;

class SimpleEntry {
    public Object key;
    public Object value;

    public SimpleEntry(final Object key, final Object value) {
        this.key = key;
        this.value = value;
    }

    public SimpleEntry(final Map.Entry entry) {
        this(entry.getKey(), entry.getValue());
    }
}
