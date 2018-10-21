package it.xdnl.hazelcast.monitor.agent.utils;

import java.util.function.Predicate;

public abstract class PredicateUtils {

    /**
     * Applies a predicate and swallows any exception that may be thrown.
     * @param predicate
     * @param value
     * @return
     */
    public static boolean safePredicateApply(final Predicate predicate, final Object value) {
        try {
            return predicate.test(value);
        } catch (Exception e) {
            return false;
        }
    }
}
