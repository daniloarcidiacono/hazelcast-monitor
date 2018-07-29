package it.xdnl.hazelcast.monitor.agent.product;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class ListProduct implements Product {
    public static class Entry {
        private final JsonNode value;
        private final String valueString;

        public Entry(final JsonNode value, final String valueString) {
            this.value = value;
            this.valueString = valueString;
        }

        public JsonNode getValue() {
            return value;
        }

        public String getValueString() {
            return valueString;
        }
    }

    private List<Entry> entries = new ArrayList<>();

    public ListProduct() {
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
