package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonExecutorService;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonRunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test component for cardinality estimators.
 * @see CardinalityEstimator
 */
public class CardinalityEstimatorComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CardinalityEstimatorComponent.class);
    private final PoissonExecutorService executorService;
    private final Random rand = new Random();
    private CardinalityEstimator cardinalityEstimator;
    private PoissonRunnableWrapper poissonRunnable;

    public CardinalityEstimatorComponent(final HazelcastInstance hazelcastInstance, final ScheduledExecutorService threadPool) {
        executorService = new PoissonExecutorService(threadPool);
        cardinalityEstimator = hazelcastInstance.getCardinalityEstimator("test_card_estimator");
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
            cardinalityEstimator.add(rand.nextInt());
        } catch (HazelcastInstanceNotActiveException e){
            // This happens when killing the JVM, just stop
            poissonRunnable.stop();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the cardinality estimator", e);
            poissonRunnable.stop();
        }
    }
}
