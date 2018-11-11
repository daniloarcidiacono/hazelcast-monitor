package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.configuration;

import io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.property.MonitorWebSocketProperties;
import io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.websocket.MonitorWebSocketEntrypointHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class MonitorWebSocketConfiguration implements WebSocketConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(MonitorWebSocketConfiguration.class);

    @Autowired
    private MonitorWebSocketEntrypointHandler webSocketEntrypointHandler;

    @Autowired
    private MonitorWebSocketProperties websocketPropertiesHazelcastMonitor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        logger.info("Mapped URL path [{}] as Hazelcast Monitor entrypoint", websocketPropertiesHazelcastMonitor.getEndpoint());

        registry.addHandler(webSocketEntrypointHandler, websocketPropertiesHazelcastMonitor.getEndpoint())
                .setAllowedOrigins(websocketPropertiesHazelcastMonitor.getAllowedOrigins().toArray(new String[websocketPropertiesHazelcastMonitor.getAllowedOrigins().size()]))
                .withSockJS();
    }
}