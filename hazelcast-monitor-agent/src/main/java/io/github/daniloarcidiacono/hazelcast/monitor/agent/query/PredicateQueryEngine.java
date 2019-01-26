package io.github.daniloarcidiacono.hazelcast.monitor.agent.query;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.*;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.utils.PredicateUtils;

import javax.cache.Cache;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateQueryEngine {
    private IExecutorService getExecutorService(final HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getExecutorService("_hzMonitor_predicateSearch");
    }

    public <T> List<T> queryList(final IList<T> list, final Predicate predicate, final HazelcastInstance hazelcastInstance) throws PredicateQueryEngineException {
        try {
            final IExecutorService executorService = getExecutorService(hazelcastInstance);
            final ListQueryTask<T> task = new ListQueryTask(list.getName(), predicate);
            final Future<List<T>> future = executorService.submitToKeyOwner(task, list.getPartitionKey());
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PredicateQueryEngineException("Error while querying list " + list.getName(), e);
        }
    }

    public <T> List<T> queryQueue(final IQueue<T> queue, final Predicate predicate, final HazelcastInstance hazelcastInstance) throws PredicateQueryEngineException {
        try {
            final IExecutorService executorService = getExecutorService(hazelcastInstance);
            final QueueQueryTask<T> task = new QueueQueryTask(queue.getName(), predicate);
            final Future<List<T>> future = executorService.submitToKeyOwner(task, queue.getPartitionKey());
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PredicateQueryEngineException("Error while querying queue " + queue.getName(), e);
        }
    }

    public <T> List<T> querySet(final ISet<T> set, final Predicate predicate, final HazelcastInstance hazelcastInstance) throws PredicateQueryEngineException {
        try {
            final IExecutorService executorService = getExecutorService(hazelcastInstance);
            final SetQueryTask<T> task = new SetQueryTask(set.getName(), predicate);
            final Future<List<T>> future = executorService.submitToKeyOwner(task, set.getPartitionKey());
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new PredicateQueryEngineException("Error while querying set " + set.getName(), e);
        }
    }

    public <K, V> List<Cache.Entry<K, V>> queryCache(final ICache<K, V> cache, final Predicate predicate, final HazelcastInstance hazelcastInstance) {
        try {
            final IExecutorService executorService = getExecutorService(hazelcastInstance);
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

    public <K, V> List<Map.Entry<K, V>> queryReplicatedMap(final ReplicatedMap<K, V> map, final Predicate predicate) {
        if (predicate instanceof ScriptPredicate) {
            ((ScriptPredicate)predicate).prepare();
        }

        final List<Map.Entry<K, V>> result = new ArrayList<>();
        for (Map.Entry<K, V> mapEntry : map.entrySet()) {
            final SimpleEntry simpleEntry = new SimpleEntry(mapEntry);
            if (PredicateUtils.safePredicateApply(predicate, simpleEntry)) {
                result.add(mapEntry);
            }
        }

        return result;
    }

    public <K, V> List<Map.Entry<K, List<V>>> queryMultiMap(final MultiMap<K, V> map, final Predicate predicate) {
        // A MultiMap entrySet() contains duplicated entries for the same key!
        final Map<K, List<V>> collapsedMap = new HashMap<>();
        for (Map.Entry<K, V> kvEntry : map.entrySet()) {
            if (!collapsedMap.containsKey(kvEntry.getKey())) {
                collapsedMap.put(kvEntry.getKey(), new ArrayList<>());
            }

            collapsedMap.get(kvEntry.getKey()).add(kvEntry.getValue());
        }

        if (predicate instanceof ScriptPredicate) {
            ((ScriptPredicate)predicate).prepare();
        }

        // ReplicatedMap does not support predicates, so we filter manually
        // TODO: The values order is random!
        return collapsedMap.entrySet().stream().filter(mapEntry -> {
            // Notice that we are passing the whole collection as the value
            final SimpleEntry simpleEntry = new SimpleEntry(mapEntry.getKey(), mapEntry.getValue());
            return PredicateUtils.safePredicateApply(predicate, simpleEntry);
        }).collect(Collectors.toList());
    }
}