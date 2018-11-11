package io.github.daniloarcidiacono.hazelcast.monitor.agent;

public interface ClientConnectionListener {
    default void received(final ClientConnection connection, final String payload) {
    }

    default void closed(final ClientConnection connection) {
    }
}
