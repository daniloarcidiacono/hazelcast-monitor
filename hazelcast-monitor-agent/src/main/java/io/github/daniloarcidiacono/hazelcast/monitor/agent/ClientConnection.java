package io.github.daniloarcidiacono.hazelcast.monitor.agent;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstraction of a connection used by the monitor.
 * <p>
 * Subclasses must implement the {@link #send(String)} method and are also responsible of
 * calling {@link #receive(String)} when appropriate.
 */
public abstract class ClientConnection {
    private Set<ClientConnectionListener> listeners = new HashSet<>();

    /**
     * Whether the connection should be authenticated.
     */
    private boolean authenticationRequired = true;

    /**
     * Group names for which the client connection has been authenticated.
     */
    private Set<String> authenticatedGroups = new HashSet<>();

    public ClientConnection() {
    }

    public void addListener(final ClientConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final ClientConnectionListener listener) {
        listeners.remove(listener);
    }

    public void receive(final String payload) {
        // Iterate a copy of the original set to prevent ConcurrentModificationExceptions
        for (ClientConnectionListener listener : new HashSet<>(listeners)) {
            listener.received(this, payload);
        }
    }

    public void close() {
        // Iterate a copy of the original set to prevent ConcurrentModificationExceptions
        for (ClientConnectionListener listener : new HashSet<>(listeners)) {
            listener.closed(this);
        }
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(boolean authenticationRequired) {
        if (authenticationRequired != this.authenticationRequired) {
            this.authenticationRequired = authenticationRequired;
            authenticatedGroups.clear();
        }
    }

    public boolean isAuthenticated(final String groupName) {
        return !authenticationRequired || authenticatedGroups.contains(groupName);
    }

    public void addGroupAuthentication(final String groupName) {
        if (authenticationRequired) {
            authenticatedGroups.add(groupName);
        }
    }

    public abstract void send(final String message);
}
