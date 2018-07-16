package it.xdnl.hazelcast.monitor.agent.dto.request;

import it.xdnl.hazelcast.monitor.agent.dto.Message;

public class GetClustersMessage extends Message {
    public static final String MESSAGE_TYPE = "clusters";
    public GetClustersMessage() {
        super(MESSAGE_TYPE);
    }
}
