package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test component for topics.
 * @see ITopic
 */
public class TopicComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TopicComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private final HazelcastInstance hazelcastInstance;
    private ITopic<Object> topic;
    private ScheduledFuture<?> scheduledFuture;

    public TopicComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        topic = hazelcastInstance.getTopic("test_topic");
        scheduledFuture = threadPool.scheduleWithFixedDelay(this, 0, 1, TimeUnit.SECONDS);
    }

    public void destroy() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

    @Override
    public void run() {
        try {
            topic.publish(Persons.random());
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the topic", e);
            scheduledFuture.cancel(true);
        }
    }
}
