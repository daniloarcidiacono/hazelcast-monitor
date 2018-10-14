package it.xdnl.hazelcast.monitor.agent.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IList;

import java.util.List;
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
}