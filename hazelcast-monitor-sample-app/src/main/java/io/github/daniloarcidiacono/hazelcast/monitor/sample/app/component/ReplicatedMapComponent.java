package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component for replicated maps.
 * @see ReplicatedMap
 */
public class ReplicatedMapComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ReplicatedMapComponent.class);
    private final HazelcastInstance hazelcastInstance;
    private ReplicatedMap<Object, Object> replicatedMap;

    public ReplicatedMapComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        replicatedMap = hazelcastInstance.getReplicatedMap("test_replicated_map");

        for (final Person person : Persons.random(15)) {
            replicatedMap.put(person.getName(), person);
        }
    }

    @Override
    public void run() {
    }
}
