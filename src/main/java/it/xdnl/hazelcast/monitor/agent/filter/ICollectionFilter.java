package it.xdnl.hazelcast.monitor.agent.filter;

/**
 * Filter for a collection of objects.
 */
public interface ICollectionFilter extends IFilter {
    boolean matches(final Object element);
}
