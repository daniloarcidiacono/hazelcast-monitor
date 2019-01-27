package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class for the monitor.
 * @see MonitorThreadsProperties
 * @see MonitorWebSocketProperties
 */
@Configuration
@ConfigurationProperties("monitor")
public class MonitorProperties {
    /**
     * Whether the monitor should be enabled or not.
     */
    private boolean enabled = true;

    /**
     * Whether the connection should be authenticated or not.
     */
    private boolean secure = true;

    /**
     * Thread-related properties
     */
    @NestedConfigurationProperty
    private MonitorThreadsProperties threads;

    /**
     * Websocket properties
     */
    @NestedConfigurationProperty
    private MonitorWebSocketProperties websocket;

    public MonitorProperties() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public MonitorThreadsProperties getThreads() {
        return threads;
    }

    public void setThreads(MonitorThreadsProperties threads) {
        this.threads = threads;
    }

    public MonitorWebSocketProperties getWebsocket() {
        return websocket;
    }

    public void setWebsocket(MonitorWebSocketProperties websocket) {
        this.websocket = websocket;
    }
}