package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Represents a set of hazelcast components tied to a specific instance.
 * @see HazelcastAppFactory
 */
public class HazelcastApp {
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(16);
    private AtomicLongComponent atomicLongComponent;
    private AtomicReferenceComponent atomicReferenceComponent;
    private CacheComponent cacheComponent;
    private CardinalityEstimatorComponent cardinalityEstimatorComponent;
    private CountDownLatchComponent countDownLatchComponent;
    private ListComponent listComponent;
    private LocksComponent locksComponent;
    private MapComponent mapComponent;
    private MultiMapComponent multiMapComponent;
    private QueuesComponent queuesComponent;
    private ReplicatedMapComponent replicatedMapComponent;
    private RingbufferComponent ringbufferComponent;
    private SemaphoreComponent semaphoreComponent;
    private SetComponent setComponent;
    private TopicComponent topicComponent;

    public HazelcastApp() {
    }

    public void configure(final Config config) {
        CacheComponent.configure(config);
    }

    public void create(final HazelcastInstance hazelcastInstance) {
        this.atomicLongComponent = new AtomicLongComponent(hazelcastInstance, threadPool);
        this.atomicReferenceComponent = new AtomicReferenceComponent(hazelcastInstance, threadPool);
        this.cacheComponent = new CacheComponent(hazelcastInstance, threadPool);
        this.cardinalityEstimatorComponent = new CardinalityEstimatorComponent(hazelcastInstance, threadPool);
        this.countDownLatchComponent = new CountDownLatchComponent(hazelcastInstance, threadPool);
        this.listComponent = new ListComponent(hazelcastInstance);
        this.locksComponent = new LocksComponent(hazelcastInstance, threadPool);
        this.mapComponent = new MapComponent(hazelcastInstance);
        this.multiMapComponent = new MultiMapComponent(hazelcastInstance);
        this.queuesComponent = new QueuesComponent(hazelcastInstance);
        this.replicatedMapComponent = new ReplicatedMapComponent(hazelcastInstance);
        this.ringbufferComponent = new RingbufferComponent(hazelcastInstance);
        this.semaphoreComponent = new SemaphoreComponent(hazelcastInstance);
        this.setComponent = new SetComponent(hazelcastInstance);
        this.topicComponent = new TopicComponent(hazelcastInstance, threadPool);
    }

    public void destroy() {
        if (atomicLongComponent != null) {
            atomicLongComponent.destroy();
            atomicLongComponent = null;
        }

        if (atomicReferenceComponent != null) {
            atomicReferenceComponent.destroy();
            atomicReferenceComponent = null;
        }

        if (cacheComponent != null) {
            cacheComponent.destroy();
            cacheComponent = null;
        }

        if (cardinalityEstimatorComponent != null) {
            cardinalityEstimatorComponent.destroy();
            cardinalityEstimatorComponent = null;
        }

        if (countDownLatchComponent != null) {
            countDownLatchComponent.destroy();
            countDownLatchComponent = null;
        }

        if (locksComponent != null) {
            locksComponent.destroy();
            locksComponent = null;
        }

        if (semaphoreComponent != null) {
            semaphoreComponent.destroy();
            semaphoreComponent = null;
        }

        if (topicComponent != null) {
            topicComponent.destroy();
            topicComponent = null;
        }
    }
}
