package it.xdnl.hazelcast.monitor.agent.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IList;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Searches in a {@link IList} using a {@link Predicate}.
 *
 * @param <T>
 * @see PredicateQueryEngine#queryList(IList, Predicate)
 */
class ListQueryTask<T> implements Callable<List<T>>, DataSerializable, HazelcastInstanceAware {
    private HazelcastInstance hazelcastInstance;
    private String listName;
    private Predicate predicate;

    public ListQueryTask() {
    }

    public ListQueryTask(final String listName, final Predicate predicate) {
        this.listName = listName;
        this.predicate = predicate;
    }

    @Override
    public List<T> call() throws Exception {
        final IList<T> list = hazelcastInstance.getList(listName);
        if (predicate instanceof ScriptPredicate) {
            ((ScriptPredicate)predicate).prepare();
        }

        return list.stream()
            .filter(this::safePredicateApply)
            .collect(Collectors.toList());
    }

    /**
     * Applies a predicate and swallows any exception that may be thrown.
     * @param value
     * @return
     */
    private boolean safePredicateApply(final T value) {
        try {
            return predicate.test(value);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(listName);
        out.writeObject(predicate);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        listName = in.readUTF();
        predicate = in.readObject();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
