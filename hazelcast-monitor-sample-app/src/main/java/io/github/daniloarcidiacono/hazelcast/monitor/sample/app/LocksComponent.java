package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
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
 * Test component for locks.
 * @see ILock
 */
@Component
public class LocksComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(LocksComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
//    private final Random rand = new Random();

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private ILock lock;
    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        lock = hazelcastInstance.getLock("test_lock");
        scheduledFuture = threadPool.scheduleWithFixedDelay(this, 0, 10, TimeUnit.SECONDS);
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
            lock.lock(8, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the lock", e);
            scheduledFuture.cancel(true);
        }
    }
}
