package it.xdnl.hazelcast.monitor.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.xdnl.hazelcast.monitor.dto.WebSocketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class WebSocketEntrypointHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEntrypointHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    // Note: when called separately, each request is assigned to a new thread.
    // When called in burst, the same thread handles all the requests.
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        try {
            final WebSocketDTO dto = mapper.readValue(message.getPayload(), WebSocketDTO.class);
            logger.info(dto.toString());
            logger.info(dto.getClass().getSimpleName());
        } catch (Exception e) {
            logger.error("Uncaught exception", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("New websocket connection {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Websocket connection closed {}", session.getId());
    }
}