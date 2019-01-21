package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Test component for maps.
 * @see IMap
 */
@Component
public class MapComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MapComponent.class);
//    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
//    private final Random rand = new Random();

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private IMap<Object, Object> map;
//    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        map = hazelcastInstance.getMap("test_map");

        for (final Person person : Persons.random(15)) {
            map.put(person.getName(), person);
        }

//        scheduledFuture = threadPool.scheduleWithFixedDelay(this, 0, 1, TimeUnit.SECONDS);
    }

//    @PreDestroy
//    public void destroy() {
//        if (scheduledFuture != null) {
//            scheduledFuture.cancel(true);
//        }
//    }

    @Override
    public void run() {
//        try {
//        } catch (Exception e) {
//            logger.error("Exception occurred when modifying the list", e);
//            scheduledFuture.cancel(true);
//        }
    }
}
