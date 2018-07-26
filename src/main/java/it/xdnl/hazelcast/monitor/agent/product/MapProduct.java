package it.xdnl.hazelcast.monitor.agent.product;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapProduct implements Product {
    public static class Entry {
        private final JsonNode key;
        private final JsonNode value;
        private final String keyString;
        private final String valueString;

        public Entry(final JsonNode key, final JsonNode value, final String keyString, final String valueString) {
            this.key = key;
            this.value = value;
            this.keyString = keyString;
            this.valueString = valueString;
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
    }
    private List<Entry> data = new ArrayList<>();

    public MapProduct() {
    }

    public void add(final Entry entry)  {
        data.add(entry);
    }

    public List<Entry> getData() {
        return data;
    }

    public void setData(List<Entry> data) {
        this.data = data;
    }
}
