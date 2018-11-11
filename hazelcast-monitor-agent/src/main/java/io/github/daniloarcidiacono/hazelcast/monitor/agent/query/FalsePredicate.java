package io.github.daniloarcidiacono.hazelcast.monitor.agent.query;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * Predicate that discards every entry.
 */
public class FalsePredicate implements Predicate<Object>, Serializable {
    public static final FalsePredicate INSTANCE = new FalsePredicate();

    private FalsePredicate() {
    }

    @Override
    public boolean test(final Object element) {
        return false;
    }
}
