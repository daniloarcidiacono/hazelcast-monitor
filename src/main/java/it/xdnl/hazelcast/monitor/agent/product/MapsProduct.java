package it.xdnl.hazelcast.monitor.agent.product;

import it.xdnl.chat.typescript.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

public class MapsProduct implements Product {
    @TypescriptDTO
    public static class MapSummaryProduct {
        private String name;
        private int size;

        public MapSummaryProduct(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    private List<MapSummaryProduct> maps = new ArrayList<>();

    public MapsProduct() {
    }

    public List<MapSummaryProduct> getMaps() {
        return maps;
    }

    public void setMaps(List<MapSummaryProduct> maps) {
        this.maps = maps;
    }
}
