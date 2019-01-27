package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * Websocket properties.
 * @see MonitorProperties
 */
@Configuration
@ConfigurationProperties("monitor.websocket")
public class MonitorWebSocketProperties {
    /**
     * Whether to use SockJS.
     */
    private boolean useSockJS = true;

    /**
     * The endpoint to which bind the websocket handler.
     */
    private String endpoint = "/monitor/ws";

    /**
     * Allowed origins for websocket handshake.
     */
    private List<String> allowedOrigins = Collections.singletonList("*");

    public MonitorWebSocketProperties() {
    }

    public boolean isUseSockJS() {
        return useSockJS;
    }

    public void setUseSockJS(boolean useSockJS) {
        this.useSockJS = useSockJS;
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