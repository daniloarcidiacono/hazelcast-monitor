package it.xdnl.hazelcast.monitor.agent.filter;

/**
 * Filter for a map.
 */
public interface IMapFilter extends IFilter {
    boolean matches(final Object key, final Object value);
}
