package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test component for countdown latches.
 * @see ICountDownLatch
 */
@Component
public class CountDownLatchComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CountDownLatchComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private final Random rand = new Random();

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private ICountDownLatch countDownLatch;
    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        countDownLatch = hazelcastInstance.getCountDownLatch("test_count_down_latch");
        countDownLatch.trySetCount(50);
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
            countDownLatch.countDown();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the countdown latch", e);
            scheduledFuture.cancel(true);
        }
    }
}
