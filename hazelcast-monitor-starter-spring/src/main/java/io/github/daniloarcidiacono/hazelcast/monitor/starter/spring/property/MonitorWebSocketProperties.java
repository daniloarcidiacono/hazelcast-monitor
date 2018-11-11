package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("monitor.websocket")
public class MonitorWebSocketProperties {
    @NestedConfigurationProperty
    private String endpoint;

    @NestedConfigurationProperty
    private List<String> allowedOrigins = new ArrayList<>();

    public MonitorWebSocketProperties() {
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}