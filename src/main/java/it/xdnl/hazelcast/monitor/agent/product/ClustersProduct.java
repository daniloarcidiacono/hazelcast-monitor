package it.xdnl.hazelcast.monitor.agent.product;

import java.util.ArrayList;
import java.util.List;

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
