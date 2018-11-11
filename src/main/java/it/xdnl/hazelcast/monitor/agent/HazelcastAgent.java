package it.xdnl.hazelcast.monitor.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import it.xdnl.hazelcast.monitor.agent.dto.ErrorMessage;
import it.xdnl.hazelcast.monitor.agent.handler.MessageHandler;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HazelcastAgent implements ClientConnectionListener {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(HazelcastAgent.class);

    private Set<ClientConnection> connections = new HashSet<>();
    private Set<MessageHandler> handlers = new HashSet<>();

    public HazelcastAgent() {
    }

    public void addConnection(final ClientConnection connection) {
        connection.addListener(this);
        connections.add(connection);
    }

    public void removeConnection(final ClientConnection connection) {
        connection.removeListener(this);
        connections.remove(connection);
    }

    public void addHandler(final MessageHandler handler) {
        handlers.add(handler);
    }

    public void removeHandler(final MessageHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public void received(final ClientConnection connection, final String payload) {
        AbstractMessage message;
        try {
            message = mapper.readValue(payload, AbstractMessage.class);
        } catch (IOException e) {
            ClientConnectionUtils.convertAndSend(connection, new ErrorMessage("Malformed message"));
            return;
        }

        try {
            boolean processed = false;
            for (MessageHandler handler : handlers) {
                if (handler.supports(message)) {
                    handler.process(connection, message);
                    processed = true;
                    break;
                }
            }

            if (!processed) {
                ClientConnectionUtils.convertAndSend(connection, new ErrorMessage("Unrecognized message"));
            }
        } catch (Exception e) {
            ClientConnectionUtils.convertAndSend(connection, new ErrorMessage("Internal server error"));
            logger.error("Error while processing message", e);
        }
    }

    @Override
    public void closed(final ClientConnection connection) {
        connection.removeListener(this);
        connections.remove(connection);
    }
}