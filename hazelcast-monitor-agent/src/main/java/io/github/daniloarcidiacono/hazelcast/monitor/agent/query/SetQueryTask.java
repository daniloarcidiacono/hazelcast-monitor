package io.github.daniloarcidiacono.hazelcast.monitor.agent.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.ISet;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.utils.PredicateUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Searches in a {@link ISet} using a {@link Predicate}.
 *
 * @param <T>
 * @see PredicateQueryEngine#querySet(ISet, Predicate)
 */
class SetQueryTask<T> implements Callable<List<T>>, DataSerializable, HazelcastInstanceAware {
    private HazelcastInstance hazelcastInstance;
    private String setName;
    private Predicate predicate;

    public SetQueryTask() {
    }

    public SetQueryTask(final String setName, final Predicate predicate) {
        this.setName = setName;
        this.predicate = predicate;
    }

    @Override
    public List<T> call() throws Exception {
        final ISet<T> set = hazelcastInstance.getSet(setName);
        if (predicate instanceof ScriptPredicate) {
            ((ScriptPredicate)predicate).prepare();
        }

        return set.stream()
            .filter(element -> PredicateUtils.safePredicateApply(predicate, element))
            .collect(Collectors.toList());
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(setName);
        out.writeObject(predicate);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        setName = in.readUTF();
        predicate = in.readObject();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
