package it.xdnl.hazelcast.monitor.agent.handler;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;

public interface MessageHandler {
    boolean supports(final AbstractMessage message);
    void process(final ClientConnection clientConnection, final AbstractMessage message);
}
