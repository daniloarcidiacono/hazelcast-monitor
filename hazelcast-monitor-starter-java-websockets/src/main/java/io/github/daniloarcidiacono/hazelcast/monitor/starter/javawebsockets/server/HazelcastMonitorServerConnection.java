package io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.server;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.ClientConnection;
import org.java_websocket.WebSocket;

/**
 * Implementation of {@link ClientConnection} that uses the {@link WebSocket} class for sending messages.
 * @see HazelcastMonitorServer
 */
public class HazelcastMonitorServerConnection extends ClientConnection {
    private final WebSocket socket;

    public HazelcastMonitorServerConnection(WebSocket socket) {
        this.socket = socket;
    }

    @Override
    public void send(String message) {
        try {
            socket.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Could not write to connection " + socket.<String>getAttachment(), e);
        }
    }
}
