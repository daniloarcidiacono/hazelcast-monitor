package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

import java.io.Serializable;

@TypescriptDTO
public class Cluster implements Serializable {
    private String instanceName;
    private String groupName;

    public Cluster(final String instanceName, final String groupName) {
        this.instanceName = instanceName;
        this.groupName = groupName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public Cluster setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public Cluster setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }
}
