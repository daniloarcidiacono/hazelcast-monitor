package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.hazelcast.core.IAtomicReference;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonExecutorService;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonRunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test component for atomic references.
 * @see IAtomicReference
 */
public class AtomicReferenceComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AtomicReferenceComponent.class);
    private final PoissonExecutorService executorService;
    private final Random rand = new Random();
    private IAtomicReference<Object> atomicReference;
    private PoissonRunnableWrapper poissonRunnable;

    public AtomicReferenceComponent(final HazelcastInstance hazelcastInstance, final ScheduledExecutorService threadPool) {
        executorService = new PoissonExecutorService(threadPool);
        atomicReference = hazelcastInstance.getAtomicReference("test_atomic_ref");
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
            atomicReference.set(rand.nextInt(100));
        } catch (HazelcastInstanceNotActiveException e){
            // This happens when killing the JVM, just stop
            poissonRunnable.stop();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the atomic reference", e);
            poissonRunnable.stop();
        }
    }
}
