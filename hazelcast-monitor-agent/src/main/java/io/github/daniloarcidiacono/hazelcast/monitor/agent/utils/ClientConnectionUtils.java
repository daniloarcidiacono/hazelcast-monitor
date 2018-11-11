package io.github.daniloarcidiacono.hazelcast.monitor.agent.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.ClientConnection;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;

public abstract class ClientConnectionUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void convertAndSend(final ClientConnection connection, final AbstractMessage message) {
        try {
            connection.send(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            // Eat the exception
        }
    }

    public static void convertAndReply(final ClientConnection connection, final AbstractMessage request, final AbstractMessage reply) {
        try {
            reply.setMessageId(request.getMessageId());
            connection.send(mapper.writeValueAsString(reply));
        } catch (JsonProcessingException e) {
            // Eat the exception
        }
    }
}
