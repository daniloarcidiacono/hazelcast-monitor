package it.xdnl.hazelcast.monitor.agent.producer;

import it.xdnl.hazelcast.monitor.agent.product.Product;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTopicProducer extends AbstractTopicProducer implements Runnable {
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
        scheduledFuture = threadPool.scheduleWithFixedDelay(this, 0, delay, timeUnit);
    }

    @Override
    public void stop() {
        scheduledFuture.cancel(false);
    }

    @Override
    public final void run() {
        notice(produce());
    }

    @Override
    public Product produce() {
        return producer.produce();
    }
}
