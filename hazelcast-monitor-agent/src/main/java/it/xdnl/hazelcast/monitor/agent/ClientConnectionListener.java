package it.xdnl.hazelcast.monitor.agent;

public interface ClientConnectionListener {
    default void received(final ClientConnection connection, final String payload) {
    }

    default void closed(final ClientConnection connection) {
    }
}
