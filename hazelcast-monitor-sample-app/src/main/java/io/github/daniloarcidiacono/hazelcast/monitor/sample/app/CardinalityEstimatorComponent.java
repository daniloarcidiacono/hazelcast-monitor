package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.core.HazelcastInstance;
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
 * Test component for cardinality estimators.
 * @see CardinalityEstimator
 */
@Component
public class CardinalityEstimatorComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CardinalityEstimatorComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private final Random rand = new Random();

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private CardinalityEstimator cardinalityEstimator;
    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        cardinalityEstimator = hazelcastInstance.getCardinalityEstimator("test_card_estimator");
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
            cardinalityEstimator.add(rand.nextInt());
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the cardinality estimator", e);
            scheduledFuture.cancel(true);
        }
    }
}
