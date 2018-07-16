package it.xdnl.hazelcast.monitor.agent.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.Message;

public abstract class ClientConnectionUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void convertAndSend(final ClientConnection connection, final Message message) {
        try {
            connection.send(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            // Eat the exception
        }
    }
}
