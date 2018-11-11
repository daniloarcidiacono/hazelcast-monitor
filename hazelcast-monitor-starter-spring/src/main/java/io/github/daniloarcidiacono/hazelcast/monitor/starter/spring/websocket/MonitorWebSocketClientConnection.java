package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.websocket;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.ClientConnection;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class MonitorWebSocketClientConnection extends ClientConnection {
    private WebSocketSession session;

    public MonitorWebSocketClientConnection(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void send(final String message) {
        try {
            session.sendMessage(new TextMessage(message.getBytes()));
        } catch (IOException e) {
            // Eat the exception
        }
    }
}
