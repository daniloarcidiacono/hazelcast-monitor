package io.github.daniloarcidiacono.hazelcast.monitor.agent.query;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * Predicate that discards every entry.
 */
public class TruePredicate implements Predicate<Object>, Serializable {
    public static final TruePredicate INSTANCE = new TruePredicate();

    private TruePredicate() {
    }

    @Override
    public boolean test(final Object element) {
        return true;
    }
}
