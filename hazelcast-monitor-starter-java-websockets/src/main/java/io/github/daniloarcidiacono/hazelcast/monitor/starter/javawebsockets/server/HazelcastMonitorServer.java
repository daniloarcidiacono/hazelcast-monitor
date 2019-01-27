package io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.server;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.HazelcastAgent;
import io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.HazelcastMonitorServerBuilder;
import io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.utils.SessionIdGenerator;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Websocket server that drives an {@link HazelcastAgent} instance.
 * @see HazelcastMonitorServerBuilder
 */
public class HazelcastMonitorServer extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(HazelcastMonitorServer.class);

    /**
     * Generator for websocket session ids.
     */
    private SessionIdGenerator sessionIdGenerator = new SessionIdGenerator();

    /**
     * The Hazelcast agent instance (cannot be null).
     */
    private HazelcastAgent hazelcastAgent;

    /**
     * Active connections map.
     */
    private ConcurrentHashMap<String, HazelcastMonitorServerConnection> connections = new ConcurrentHashMap<>();

    public HazelcastMonitorServer(final int port) {
        super(new InetSocketAddress(port));
    }

    public HazelcastMonitorServer(final InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        // Generate a session id
        String sessionId = sessionIdGenerator.generateSessionId();
        int attempts = 1;

        // This loop should always be skipped!
        while (connections.containsKey(sessionId) && attempts < 10) {
            sessionId = sessionIdGenerator.generateSessionId();
            attempts++;
        }

        if (!connections.containsKey(sessionId)) {
            // Normal path
            webSocket.setAttachment(sessionId);
            logger.info("New websocket connection {}", sessionId);

            final HazelcastMonitorServerConnection connection = new HazelcastMonitorServerConnection(webSocket);
            hazelcastAgent.addConnection(connection);
            connections.put(sessionId, connection);
        } else {
            // Weird path
            logger.error("Could not generate unique id for connection");
            webSocket.close(CloseFrame.REFUSE, "Could not generate unique id for connection!");
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        final String sessionId = webSocket.getAttachment();
        logger.info("Websocket connection closed {}", sessionId);
        final HazelcastMonitorServerConnection connection = connections.get(sessionId);
        connection.close();
        connections.remove(sessionId);
    }

    // One thread per connection
    @Override
    public void onMessage(WebSocket webSocket, String message) {
        final String sessionId = webSocket.getAttachment();
        final HazelcastMonitorServerConnection connection = connections.get(sessionId);
        assert connection != null;

        connection.receive(message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error("Error on connection {}", webSocket != null ? webSocket.getResourceDescriptor() : "n/a", e);
    }

    @Override
    public void onStart() {
        logger.info("Hazelcast Monitor started");
    }

    public HazelcastAgent getHazelcastAgent() {
        return hazelcastAgent;
    }

    public void setHazelcastAgent(HazelcastAgent hazelcastAgent) {
        this.hazelcastAgent = hazelcastAgent;
    }
}
