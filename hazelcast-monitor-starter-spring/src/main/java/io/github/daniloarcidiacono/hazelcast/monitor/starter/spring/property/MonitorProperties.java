package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("monitor")
public class MonitorProperties {
    private boolean enabled = true;

    @NestedConfigurationProperty
    private MonitorThreadsProperties threadsProperties;

    @NestedConfigurationProperty
    private MonitorWebSocketProperties webSocketProperties;

    public MonitorProperties() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MonitorThreadsProperties getThreadsProperties() {
        return threadsProperties;
    }

    public void setThreadsProperties(MonitorThreadsProperties threadsProperties) {
        this.threadsProperties = threadsProperties;
    }

    public MonitorWebSocketProperties getWebSocketProperties() {
        return webSocketProperties;
    }

    public void setWebSocketProperties(MonitorWebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }
}