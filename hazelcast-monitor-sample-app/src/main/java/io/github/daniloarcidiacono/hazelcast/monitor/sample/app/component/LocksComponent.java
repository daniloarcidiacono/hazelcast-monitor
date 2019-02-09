package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.hazelcast.core.ILock;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonExecutorService;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonRunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test component for locks.
 * @see ILock
 */
public class LocksComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(LocksComponent.class);
    private final PoissonExecutorService executorService;
    private ILock lock;
    private PoissonRunnableWrapper poissonRunnable;

    public LocksComponent(final HazelcastInstance hazelcastInstance, final ScheduledExecutorService threadPool) {
        executorService = new PoissonExecutorService(threadPool);
        lock = hazelcastInstance.getLock("test_lock");
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
            lock.lock(8, TimeUnit.SECONDS);
        } catch (HazelcastInstanceNotActiveException e){
            // This happens when killing the JVM, just stop
            poissonRunnable.stop();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the lock", e);
            poissonRunnable.stop();
        }
    }
}
