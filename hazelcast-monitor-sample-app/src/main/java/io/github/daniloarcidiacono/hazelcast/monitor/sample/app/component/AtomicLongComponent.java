package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.*;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonExecutorService;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonRunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Test component for atomic longs.
 * @see IAtomicLong
 */
public class AtomicLongComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AtomicLongComponent.class);
    private final PoissonExecutorService executorService;
    private IAtomicLong atomicLong;
    private PoissonRunnableWrapper poissonRunnable;

    public AtomicLongComponent(final HazelcastInstance hazelcastInstance, final ScheduledExecutorService threadPool) {
        executorService = new PoissonExecutorService(threadPool);
        atomicLong = hazelcastInstance.getAtomicLong("test_atomic_long");
        poissonRunnable = executorService.scheduleAsPoissonProcess(this, 60, TimeUnit.MINUTES);
    }

    public void destroy() {
        if (poissonRunnable != null) {
            poissonRunnable.stop();
            poissonRunnable = null;
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
        } catch (HazelcastInstanceNotActiveException e){
            // This happens when killing the JVM, just stop
            poissonRunnable.stop();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the atomic long", e);
            poissonRunnable.stop();
        }
    }
}
