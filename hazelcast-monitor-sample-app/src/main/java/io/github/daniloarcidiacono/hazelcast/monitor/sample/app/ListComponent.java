package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Test component for lists.
 * @see IList
 */
@Component
public class ListComponent {
    private static final Logger logger = LoggerFactory.getLogger(ListComponent.class);

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private IList<Object> list;

    @PostConstruct
    private void init() {
        list = hazelcastInstance.getList("test_list");
        list.addAll(Persons.random(2));
        list.add("Some string");
    }
}
