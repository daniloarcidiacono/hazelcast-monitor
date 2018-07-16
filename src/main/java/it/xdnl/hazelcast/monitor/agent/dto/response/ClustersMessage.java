package it.xdnl.hazelcast.monitor.agent.dto.response;

import it.xdnl.hazelcast.monitor.agent.dto.Message;

public class ClustersMessage extends Message {
    public static final String MESSAGE_TYPE = "clusters";
    public ClustersMessage() {
        super(MESSAGE_TYPE);
    }
}
