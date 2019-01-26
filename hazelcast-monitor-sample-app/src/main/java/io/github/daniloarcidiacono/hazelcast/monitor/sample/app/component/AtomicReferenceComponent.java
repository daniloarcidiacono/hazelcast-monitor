package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test component for atomic references.
 * @see IAtomicReference
 */
public class AtomicReferenceComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AtomicReferenceComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private final Random rand = new Random();
    private final HazelcastInstance hazelcastInstance;
    private IAtomicReference<Object> atomicReference;
    private ScheduledFuture<?> scheduledFuture;

    public AtomicReferenceComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        atomicReference = hazelcastInstance.getAtomicReference("test_atomic_ref");
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
            atomicReference.set(rand.nextInt(100));
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the atomic reference", e);
            scheduledFuture.cancel(true);
        }
    }
}
