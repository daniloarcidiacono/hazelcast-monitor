package it.xdnl.hazelcast.monitor.spring.configuration;

import it.xdnl.hazelcast.monitor.spring.property.MonitorWebSocketProperties;
import it.xdnl.hazelcast.monitor.spring.websocket.MonitorWebSocketEntrypointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class MonitorWebSocketConfiguration implements WebSocketConfigurer {
    @Autowired
    private MonitorWebSocketEntrypointHandler webSocketEntrypointHandler;

    @Autowired
    private MonitorWebSocketProperties websocketPropertiesHazelcastMonitor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketEntrypointHandler, websocketPropertiesHazelcastMonitor.getEndpoint())
                .setAllowedOrigins(websocketPropertiesHazelcastMonitor.getAllowedOrigins().toArray(new String[websocketPropertiesHazelcastMonitor.getAllowedOrigins().size()]))
                .withSockJS();
    }
}