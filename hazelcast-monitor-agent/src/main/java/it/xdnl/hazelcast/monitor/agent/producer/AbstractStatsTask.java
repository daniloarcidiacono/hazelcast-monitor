package it.xdnl.hazelcast.monitor.agent.producer;

import java.io.Serializable;
import java.util.concurrent.Callable;

public abstract class AbstractStatsTask<T> implements Callable<T>, Serializable {
    protected final String instanceName;
    protected final String objectName;

    public AbstractStatsTask(final String instanceName, final String objectName) {
        this.instanceName = instanceName;
        this.objectName = objectName;
    }

    public abstract T call() throws Exception;

    public String getInstanceName() {
        return instanceName;
    }

    public String getObjectName() {
        return objectName;
    }
}
