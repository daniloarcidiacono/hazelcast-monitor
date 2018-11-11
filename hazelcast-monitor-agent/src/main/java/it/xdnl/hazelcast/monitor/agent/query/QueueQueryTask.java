package it.xdnl.hazelcast.monitor.agent.query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IList;
import com.hazelcast.core.IQueue;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import it.xdnl.hazelcast.monitor.agent.utils.PredicateUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Searches in a {@link IQueue} using a {@link Predicate}.
 *
 * @param <T>
 * @see PredicateQueryEngine#queryQueue(IQueue, Predicate)
 */
class QueueQueryTask<T> implements Callable<List<T>>, DataSerializable, HazelcastInstanceAware {
    private HazelcastInstance hazelcastInstance;
    private String queueName;
    private Predicate predicate;

    public QueueQueryTask() {
    }

    public QueueQueryTask(final String queueName, final Predicate predicate) {
        this.queueName = queueName;
        this.predicate = predicate;
    }

    @Override
    public List<T> call() throws Exception {
        final IQueue<T> queue = hazelcastInstance.getQueue(queueName);
        if (predicate instanceof ScriptPredicate) {
            ((ScriptPredicate)predicate).prepare();
        }

        return queue.stream()
            .filter(element -> PredicateUtils.safePredicateApply(predicate, element))
            .collect(Collectors.toList());
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(queueName);
        out.writeObject(predicate);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        queueName = in.readUTF();
        predicate = in.readObject();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
