package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model.Persons;
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
 * Test component for topics.
 * @see ITopic
 */
@Component
public class TopicComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TopicComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private ITopic<Object> topic;
    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        topic = hazelcastInstance.getTopic("test_topic");
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
            topic.publish(Persons.random());
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the topic", e);
            scheduledFuture.cancel(true);
        }
    }
}
