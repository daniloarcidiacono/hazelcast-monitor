package it.xdnl.hazelcast.monitor.agent.handler;

import it.xdnl.hazelcast.monitor.agent.ClientConnection;
import it.xdnl.hazelcast.monitor.agent.dto.Message;
import it.xdnl.hazelcast.monitor.agent.dto.response.ClustersMessage;

public class ClustersMessageHandler implements MessageHandler {
    @Override
    public void process(final ClientConnection connection, final Message genericMessage) {
    }

    @Override
    public boolean supports(final Message message) {
        return message instanceof ClustersMessage;
    }
}
