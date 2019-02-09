package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Test component for semaphores.
 * @see ISemaphore
 */
public class SemaphoreComponent {
    private static final Logger logger = LoggerFactory.getLogger(SemaphoreComponent.class);
    private final ExecutorService threadPool = Executors.newFixedThreadPool(2);
    private ISemaphore semaphore;
    private Future<?> consumerAFuture, consumerBFuture;

    static class SemaphoreConsumer implements Runnable {
        private static final Logger logger = LoggerFactory.getLogger(SemaphoreConsumer.class);
        private final ISemaphore semaphore;
        private final Random rand = new Random();

        SemaphoreConsumer(ISemaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // Sleep from 1 to 5 seconds
                    Thread.sleep(1 + rand.nextInt(4) * 1000);

                    // Acquire
                    semaphore.acquire();

                    // Sleep from 1 to 5 seconds
                    Thread.sleep(1 + rand.nextInt(4) * 1000);

                    // Release
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                // Just catch the error and stop the thread
            }
        }
    }

    public SemaphoreComponent(final HazelcastInstance hazelcastInstance) {
        semaphore = hazelcastInstance.getSemaphore("test_semaphore");
        semaphore.init(10);

        consumerAFuture = threadPool.submit(new SemaphoreConsumer(semaphore));
        consumerBFuture = threadPool.submit(new SemaphoreConsumer(semaphore));
    }

    public void destroy() {
        if (consumerAFuture != null) {
            consumerAFuture.cancel(true);
            consumerAFuture = null;
        }

        if (consumerBFuture != null) {
            consumerBFuture.cancel(true);
            consumerBFuture = null;
        }
    }
}
