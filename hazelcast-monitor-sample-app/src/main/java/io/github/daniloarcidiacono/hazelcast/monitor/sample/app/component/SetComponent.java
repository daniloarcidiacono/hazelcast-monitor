package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISet;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component for sets.
 * @see ISet
 */
public class SetComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SetComponent.class);
    private ISet<Object> set;

    public SetComponent(final HazelcastInstance hazelcastInstance) {
        set = hazelcastInstance.getSet("test_set");

        for (final Person person : Persons.random(4)) {
            set.add(person);
        }
    }

    @Override
    public void run() {
    }
}
