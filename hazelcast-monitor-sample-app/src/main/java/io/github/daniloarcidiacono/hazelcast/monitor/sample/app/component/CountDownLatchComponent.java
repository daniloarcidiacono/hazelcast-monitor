package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.hazelcast.core.ICountDownLatch;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonExecutorService;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonRunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test component for countdown latches.
 * @see ICountDownLatch
 */
public class CountDownLatchComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CountDownLatchComponent.class);
    private final PoissonExecutorService executorService;
    private ICountDownLatch countDownLatch;
    private PoissonRunnableWrapper poissonRunnable;

    public CountDownLatchComponent(final HazelcastInstance hazelcastInstance, final ScheduledExecutorService threadPool) {
        executorService = new PoissonExecutorService(threadPool);
        countDownLatch = hazelcastInstance.getCountDownLatch("test_count_down_latch");
        countDownLatch.trySetCount(50);
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
            countDownLatch.countDown();
        } catch (HazelcastInstanceNotActiveException e){
            // This happens when killing the JVM, just stop
            poissonRunnable.stop();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the countdown latch", e);
            poissonRunnable.stop();
        }
    }
}
