package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component for queues.
 * @see IQueue
 */
public class QueuesComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(QueuesComponent.class);
    private final HazelcastInstance hazelcastInstance;
    private IQueue<Object> queue;

    public QueuesComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        queue = hazelcastInstance.getQueue("testQueue");
        queue.addAll(Persons.random(8));
    }

    @Override
    public void run() {
    }
}
