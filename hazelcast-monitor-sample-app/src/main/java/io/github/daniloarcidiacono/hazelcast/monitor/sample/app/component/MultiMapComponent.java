package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component for multimaps.
 * @see MultiMap
 */
public class MultiMapComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MultiMapComponent.class);
    private MultiMap<Object, Object> multiMap;

    public MultiMapComponent(final HazelcastInstance hazelcastInstance) {
        multiMap = hazelcastInstance.getMultiMap("test_multimap");

        for (final Person person : Persons.random(15)) {
            multiMap.put(person.getContacts().size(), person);
        }
    }

    @Override
    public void run() {
    }
}
