package it.xdnl.hazelcast.monitor.spring.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("monitor.threads")
public class MonitorThreadsProperties {
    @NestedConfigurationProperty
    private int threadPoolSize;

    public MonitorThreadsProperties() {
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
}