package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component for lists.
 * @see IList
 */
public class ListComponent {
    private static final Logger logger = LoggerFactory.getLogger(ListComponent.class);
    private final HazelcastInstance hazelcastInstance;
    private IList<Object> list;

    public ListComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        list = hazelcastInstance.getList("test_list");
        list.addAll(Persons.random(2));
        list.add("Some string");
    }
}
