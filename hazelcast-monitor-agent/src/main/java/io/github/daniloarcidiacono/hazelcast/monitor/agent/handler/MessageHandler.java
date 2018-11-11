package io.github.daniloarcidiacono.hazelcast.monitor.agent.handler;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.ClientConnection;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.dto.AbstractMessage;

public interface MessageHandler {
    boolean supports(final AbstractMessage message);
    void process(final ClientConnection clientConnection, final AbstractMessage message);
}
