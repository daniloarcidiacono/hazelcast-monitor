package it.xdnl.hazelcast.monitor.agent.product;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class MapProduct implements Product {
    public static class Entry {
        private final JsonNode key;
        private final JsonNode value;
        private final String keyString;
        private final String valueString;
        private final boolean locked;

        public Entry(final JsonNode key, final JsonNode value, final String keyString, final String valueString, final boolean locked) {
            this.key = key;
            this.value = value;
            this.keyString = keyString;
            this.valueString = valueString;
            this.locked = locked;
        }

        public JsonNode getKey() {
            return key;
        }

        public JsonNode getValue() {
            return value;
        }

        public String getKeyString() {
            return keyString;
        }

        public String getValueString() {
            return valueString;
        }

        public boolean isLocked() {
            return locked;
        }
    }

    private List<Entry> entries = new ArrayList<>();

    public MapProduct() {
    }

    public void add(final Entry entry)  {
        entries.add(entry);
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
