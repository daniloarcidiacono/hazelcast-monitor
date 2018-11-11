package io.github.daniloarcidiacono.hazelcast.monitor.agent;

import java.util.HashSet;
import java.util.Set;

public abstract class ClientConnection {
    private Set<ClientConnectionListener> listeners = new HashSet<>();

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

    public abstract void send(final String message);
}
