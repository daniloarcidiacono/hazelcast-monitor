package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField;

import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class MapProduct extends Product {
    @TypescriptDTO
    public static class Entry {
        @TypescriptField(type = Object.class)
        private final JsonNode key;

        @TypescriptField(type = Object.class)
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
