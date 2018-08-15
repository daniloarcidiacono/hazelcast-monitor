package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class ClustersProduct implements Product {
    private List<String> clusters = new ArrayList<>();

    public void add(final String instanceName) {
        clusters.add(instanceName);
    }

    public List<String> getClusters() {
        return clusters;
    }

    public void setClusters(List<String> clusters) {
        this.clusters = clusters;
    }
}
