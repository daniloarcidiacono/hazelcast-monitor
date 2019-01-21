package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.ringbuffer.Ringbuffer;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Person;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Test component for ringbuffers.
 * @see Ringbuffer
 */
@Component
public class RingbufferComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RingbufferComponent.class);
//    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
//    private final Random rand = new Random();

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private Ringbuffer<Object> ringbuffer;
//    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        ringbuffer = hazelcastInstance.getRingbuffer("test_ringbuffer");

        for (final Person person : Persons.random(4)) {
            ringbuffer.add(person);
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
//            logger.error("Exception occurred when modifying the ringbuffer", e);
//            scheduledFuture.cancel(true);
//        }
    }
}
