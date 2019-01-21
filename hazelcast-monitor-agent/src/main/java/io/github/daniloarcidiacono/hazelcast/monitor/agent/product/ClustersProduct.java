package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@TypescriptDTO
public class ClustersProduct extends Product {
    private List<Cluster> clusters = new ArrayList<>();

    public void add(final Cluster cluster) {
        clusters.add(cluster);
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }
}
