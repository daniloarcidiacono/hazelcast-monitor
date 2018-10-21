package it.xdnl.hazelcast.monitor.agent.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import it.xdnl.hazelcast.monitor.agent.utils.PredicateUtils;

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

    public <K, V> List<Map.Entry<K, V>> queryMap(final IMap<K, V> map, final Predicate predicate) {
        return new ArrayList<>(
            map.entrySet(new com.hazelcast.query.Predicate() {
                class SimpleEntry {
                    public Object key;
                    public Object value;

                    public SimpleEntry(Object key, Object value) {
                        this.key = key;
                        this.value = value;
                    }
                }

                @Override
                public boolean apply(Map.Entry mapEntry) {
                    if (predicate instanceof ScriptPredicate) {
                        ((ScriptPredicate)predicate).prepare();
                    }

                    final SimpleEntry simpleEntry = new SimpleEntry(mapEntry.getKey(), mapEntry.getValue());
                    return PredicateUtils.safePredicateApply(predicate, simpleEntry);
                }
            })
        );
    }
}