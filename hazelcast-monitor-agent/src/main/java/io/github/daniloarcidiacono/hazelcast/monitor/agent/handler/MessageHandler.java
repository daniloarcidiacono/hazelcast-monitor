package io.github.daniloarcidiacono.hazelcast.monitor.agent.handler;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.ClientConnection;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;

public interface MessageHandler {
    /**
     * Checks if this handler supports a specific message.
     * @param message the message to check
     * @return true if the message can be processed by this handler, false otherwise.
     */
    boolean supports(final AbstractMessage message);

    /**
     * Processes a request.
     *
     * @param clientConnection the client connection from which the request came from.
     * @param message the request message
     */
    void process(final ClientConnection clientConnection, final AbstractMessage message);
}
