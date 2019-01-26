package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component for maps.
 * @see IMap
 */
public class MapComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MapComponent.class);
    private final HazelcastInstance hazelcastInstance;
    private IMap<Object, Object> map;

    public MapComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        map = hazelcastInstance.getMap("test_map");

        for (final Person person : Persons.random(15)) {
            map.put(person.getName(), person);
        }
    }

    @Override
    public void run() {
    }
}
