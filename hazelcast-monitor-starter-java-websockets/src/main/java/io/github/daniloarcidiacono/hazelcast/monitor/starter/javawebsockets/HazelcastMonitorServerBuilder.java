package io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.HazelcastAgent;
import io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.utils.SSLFactoryUtils;
import io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets.server.HazelcastMonitorServer;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import javax.net.ssl.SSLContext;

/**
 * Builder for {@link HazelcastMonitorServer} class.
 * @see HazelcastAgentBuilder
 */
public class HazelcastMonitorServerBuilder {
    /**
     * Server port
     */
    private int port = 7000;

    /**
     * HazelcastAgent instance (cannot be null)
     * @see HazelcastAgentBuilder
     */
    private HazelcastAgent hazelcastAgent;

    /**
     * Interval (in seconds) for checking lost connections.
     * A value lower or equal 0 results in the check to be deactivated.
     */
    private int connectionLostTimeout = 60;

    /**
     * An optional SSL context.
     * @see SSLFactoryUtils
     */
    private SSLContext sslContext = null;

    /**
     * Builds a {@link HazelcastMonitorServer} object.
     * <p>
     * The server must be started manually by invoking {@link HazelcastMonitorServer#start()}-
     * @return the newly created instance.
     */
    public HazelcastMonitorServer build() {
        if (hazelcastAgent == null) {
            throw new IllegalStateException("hazelcastAgent cannot be null");
        }

        final HazelcastMonitorServer server = new HazelcastMonitorServer(port);
        server.setHazelcastAgent(hazelcastAgent);
        server.setConnectionLostTimeout(connectionLostTimeout);
        if (sslContext != null) {
            server.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
        }
        return server;
    }

    public int getPort() {
        return port;
    }

    public HazelcastMonitorServerBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public HazelcastAgent getHazelcastAgent() {
        return hazelcastAgent;
    }

    public HazelcastMonitorServerBuilder setHazelcastAgent(HazelcastAgent hazelcastAgent) {
        this.hazelcastAgent = hazelcastAgent;
        return this;
    }

    public int getConnectionLostTimeout() {
        return connectionLostTimeout;
    }

    public HazelcastMonitorServerBuilder setConnectionLostTimeout(int connectionLostTimeout) {
        this.connectionLostTimeout = connectionLostTimeout;
        return this;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public HazelcastMonitorServerBuilder setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }
}
