package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.websocket;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.HazelcastAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MonitorWebSocketEntrypointHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(MonitorWebSocketEntrypointHandler.class);

    @Autowired
    private HazelcastAgent hazelcastAgent;

    private ConcurrentHashMap<String, MonitorWebSocketClientConnection> connections = new ConcurrentHashMap<>();

    // Note: when called separately, each request is assigned to a new thread.
    // When called in burst, the same thread handles all the requests.
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        final MonitorWebSocketClientConnection connection = connections.get(session.getId());
        assert connection != null;

        connection.receive(message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("New websocket connection {}", session.getId());

        final MonitorWebSocketClientConnection connection = new MonitorWebSocketClientConnection(session);
        hazelcastAgent.addConnection(connection);
        connections.put(session.getId(), connection);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Websocket connection closed {}", session.getId());
        final MonitorWebSocketClientConnection connection = connections.get(session.getId());
        connection.close();
        connections.remove(session.getId());
    }
}