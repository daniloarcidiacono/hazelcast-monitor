package it.xdnl.hazelcast.monitor.agent.query;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.*;
import it.xdnl.hazelcast.monitor.agent.utils.PredicateUtils;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;

public class PredicateQueryEngine {
    private final IExecutorService executorService;

    public PredicateQueryEngine(final HazelcastInstance hazelcastInstance) {
        this.executorService = hazelcastInstance.getExecutorService("_hzMonitor_predicateSearch");
    }

    public <T> List<T> queryList(final IList<T> list, final Predicate predicate) throws PredicateQueryEngineException {
        try {
            final ListQueryTask<T> task = new ListQueryTask(list.getName(), predicate);
            final Future<List<T>> future = executorService.submitToKeyOwner(task, list.getPartitionKey());
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PredicateQueryEngineException("Error while querying list " + list.getName(), e);
        }
    }

    public <T> List<T> queryQueue(final IQueue<T> queue, final Predicate predicate) throws PredicateQueryEngineException {
        try {
            final QueueQueryTask<T> task = new QueueQueryTask(queue.getName(), predicate);
            final Future<List<T>> future = executorService.submitToKeyOwner(task, queue.getPartitionKey());
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PredicateQueryEngineException("Error while querying queue " + queue.getName(), e);
        }
    }

    public <T> List<T> querySet(final ISet<T> set, final Predicate predicate) throws PredicateQueryEngineException {
        try {
            final SetQueryTask<T> task = new SetQueryTask(set.getName(), predicate);
            final Future<List<T>> future = executorService.submitToKeyOwner(task, set.getPartitionKey());
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PredicateQueryEngineException("Error while querying set " + set.getName(), e);
        }
    }

    public <K, V> List<Cache.Entry<K, V>> queryCache(final ICache<K, V> cache, final Predicate predicate) {

        try {
            final CacheQueryTask<K, V> task = new CacheQueryTask(cache.getName(), predicate);
            final Future<List<Cache.Entry<K, V>>> future = executorService.submitToKeyOwner(task, cache.getPartitionKey());
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PredicateQueryEngineException("Error while querying cache " + cache.getName(), e);
        }
    }

    public <K, V> List<Map.Entry<K, V>> queryMap(final IMap<K, V> map, final Predicate predicate) {
        return new ArrayList<>(
            map.entrySet((com.hazelcast.query.Predicate) mapEntry -> {
                if (predicate instanceof ScriptPredicate) {
                    ((ScriptPredicate)predicate).prepare();
                }

                final SimpleEntry simpleEntry = new SimpleEntry(mapEntry.getKey(), mapEntry.getValue());
                return PredicateUtils.safePredicateApply(predicate, simpleEntry);
            })
        );
    }
}