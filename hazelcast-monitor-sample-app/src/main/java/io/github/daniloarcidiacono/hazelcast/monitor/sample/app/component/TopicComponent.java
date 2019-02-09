package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.hazelcast.core.ITopic;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonExecutorService;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonRunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test component for topics.
 * @see ITopic
 */
public class TopicComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TopicComponent.class);
    private final PoissonExecutorService executorService;
    private ITopic<Object> topic;
    private PoissonRunnableWrapper poissonRunnable;

    public TopicComponent(final HazelcastInstance hazelcastInstance, final ScheduledExecutorService threadPool) {
        executorService = new PoissonExecutorService(threadPool);
        topic = hazelcastInstance.getTopic("test_topic");
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
            topic.publish(Persons.random());
        } catch (HazelcastInstanceNotActiveException e){
            // This happens when killing the JVM, just stop
            poissonRunnable.stop();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the topic", e);
            poissonRunnable.stop();
        }
    }
}
