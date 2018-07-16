package it.xdnl.hazelcast.monitor.configuration;

import it.xdnl.hazelcast.monitor.handler.WebSocketEntrypointHandler;
import it.xdnl.hazelcast.monitor.property.WebSocketProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Autowired
    private WebSocketProperties websocketProperties;

    @Autowired
    private WebSocketEntrypointHandler webSocketEntrypointHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketEntrypointHandler, websocketProperties.getEndpoint())
                .setAllowedOrigins(websocketProperties.getAllowedOrigins().toArray(new String[websocketProperties.getAllowedOrigins().size()]))
                .withSockJS();
    }
}