package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test component for atomic longs.
 * @see IAtomicLong
 */
public class AtomicLongComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AtomicLongComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private final HazelcastInstance hazelcastInstance;
    private IAtomicLong atomicLong;
    private ScheduledFuture<?> scheduledFuture;

    public AtomicLongComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;

        atomicLong = hazelcastInstance.getAtomicLong("test_atomic_long");
        scheduledFuture = threadPool.scheduleWithFixedDelay(this, 0, 1, TimeUnit.SECONDS);
    }

    public void destroy() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
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
