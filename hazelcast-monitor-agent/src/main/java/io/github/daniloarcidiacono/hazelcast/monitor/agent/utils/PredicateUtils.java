package io.github.daniloarcidiacono.hazelcast.monitor.agent.utils;

import java.util.function.Predicate;

public abstract class PredicateUtils {

    /**
     * Applies a predicate and swallows any exception that may be thrown.
     * @param predicate the predicate to apply (cannot be null)
     * @param value the value to which apply the predicate
     * @return true if the predicate matches, false otherwise.
     */
    public static boolean safePredicateApply(final Predicate predicate, final Object value) {
        try {
            return predicate.test(value);
        } catch (Exception e) {
            return false;
        }
    }
}
