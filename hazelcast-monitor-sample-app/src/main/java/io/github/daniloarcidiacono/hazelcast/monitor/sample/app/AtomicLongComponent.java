package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test component for atomic longs.
 * @see IAtomicLong
 */
@Component
public class AtomicLongComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AtomicLongComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private IAtomicLong atomicLong;
    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        atomicLong = hazelcastInstance.getAtomicLong("test_atomic_long");
        scheduledFuture = threadPool.scheduleWithFixedDelay(this, 0, 1, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    @Override
    public void run() {
        try {
            // Modify
            final long value = atomicLong.addAndGet(1);

            // Wrap
            if (value > 100) {
                atomicLong.set(0);
            }
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the atomic long", e);
            scheduledFuture.cancel(true);
        }
    }
}
