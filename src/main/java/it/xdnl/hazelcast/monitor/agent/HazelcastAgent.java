package it.xdnl.hazelcast.monitor.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.xdnl.hazelcast.monitor.agent.dto.ErrorMessage;
import it.xdnl.hazelcast.monitor.agent.dto.Message;
import it.xdnl.hazelcast.monitor.agent.handler.MessageHandler;
import it.xdnl.hazelcast.monitor.agent.utils.ClientConnectionUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HazelcastAgent implements ClientConnectionListener {
    private static final ObjectMapper mapper = new ObjectMapper();

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
        try {
            final Message message = mapper.readValue(payload, Message.class);
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
        } catch (IOException e) {
            ClientConnectionUtils.convertAndSend(connection, new ErrorMessage("Malformed message"));
        }
    }

    @Override
    public void closed(final ClientConnection connection) {
        connection.removeListener(this);
        connections.remove(connection);
    }
}