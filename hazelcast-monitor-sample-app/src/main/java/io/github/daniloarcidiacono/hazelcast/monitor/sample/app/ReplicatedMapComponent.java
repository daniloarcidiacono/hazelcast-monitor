package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Test component for replicated maps.
 * @see ReplicatedMap
 */
@Component
public class ReplicatedMapComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ReplicatedMapComponent.class);
//    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
//    private final Random rand = new Random();

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private ReplicatedMap<Object, Object> replicatedMap;
//    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        replicatedMap = hazelcastInstance.getReplicatedMap("test_replicated_map");

        for (final Person person : Persons.random(15)) {
            replicatedMap.put(person.getName(), person);
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
//            logger.error("Exception occurred when modifying the replicated map", e);
//            scheduledFuture.cancel(true);
//        }
    }
}
