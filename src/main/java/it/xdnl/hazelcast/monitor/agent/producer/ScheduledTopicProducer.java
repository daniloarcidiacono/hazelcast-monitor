package it.xdnl.hazelcast.monitor.agent.producer;

import it.xdnl.hazelcast.monitor.agent.exception.UpdateParameterException;
import it.xdnl.hazelcast.monitor.agent.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTopicProducer extends AbstractTopicProducer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTopicProducer.class);
    private final ScheduledExecutorService threadPool;
    private ScheduledFuture<?> scheduledFuture;
    private Long delay;
    private TimeUnit timeUnit;
    private final AbstractTopicProducer producer;

    public ScheduledTopicProducer(final AbstractTopicProducer producer,
                                  final ScheduledExecutorService threadPool,
                                  final Long delay,
                                  final TimeUnit timeUnit) {
        super(producer.getTopicType());
        this.threadPool = threadPool;
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.producer = producer;
    }

    @Override
    public void start() {
        if (scheduledFuture == null && delay > 0) {
            scheduledFuture = threadPool.scheduleWithFixedDelay(this, 0, delay, timeUnit);
        }
    }

    @Override
    public void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    @Override
    public void updateParameter(String parameter, String value) throws UpdateParameterException {
        if (parameter.equals("frequency")) {
            final long newDelay = Long.parseLong(value);

            // Don't update if the new value is the same as the old one
            if (newDelay != delay) {
                // Parse
                delay = Long.parseLong(value);

                // Clip
                if (delay < 0) {
                    delay = 0L;
                }

                // Stop & Restart
                stop();
                start();
            }
        } else {
            // Delegate
            producer.updateParameter(parameter, value);
        }
    }

    @Override
    public final void run() {
        try {
            notice(produce());
        } catch (Exception e) {
            logger.error("Producer has thrown an exception, stopping", e);
            stop();
        }
    }

    @Override
    public Product produce() {
        return producer.produce();
    }
}
