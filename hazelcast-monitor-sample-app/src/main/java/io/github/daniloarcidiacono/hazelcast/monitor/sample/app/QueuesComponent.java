package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Test component for queues.
 * @see IQueue
 */
@Component
public class QueuesComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(QueuesComponent.class);
//    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
//    private final Random rand = new Random();

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private IQueue<Object> queue;
//    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        queue = hazelcastInstance.getQueue("testQueue");
        queue.addAll(Persons.random(8));

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
