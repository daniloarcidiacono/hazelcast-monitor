package it.xdnl.hazelcast.monitor.agent.query;

/**
 * Runtime exception thrown by {@link PredicateQueryEngine} methods.
 */
public class PredicateQueryEngineException extends RuntimeException {
    public PredicateQueryEngineException() {
    }

    public PredicateQueryEngineException(String s) {
        super(s);
    }

    public PredicateQueryEngineException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PredicateQueryEngineException(Throwable throwable) {
        super(throwable);
    }

    public PredicateQueryEngineException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
