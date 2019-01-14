package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class ClustersProduct extends Product {
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
