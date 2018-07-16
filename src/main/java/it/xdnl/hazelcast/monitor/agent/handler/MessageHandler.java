package it.xdnl.hazelcast.monitor.agent.handler;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.Message;

public interface MessageHandler {
    boolean supports(final Message message);
    void process(final ClientConnection clientConnection, final Message message);
}
