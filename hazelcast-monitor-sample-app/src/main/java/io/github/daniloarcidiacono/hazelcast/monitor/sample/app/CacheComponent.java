package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import com.hazelcast.cache.ICache;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.configuration.HazelcastConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Test component for caches.
 * @see ICache
 */
@Component
public class CacheComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CacheComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private final Random rand = new Random();

    /**
     * Implements the {@link HazelcastConfigurer} as a inner class to avoid circular dependencies
     * between {@link CacheComponent} and {@link HazelcastInstance}.
     */
    @Component
    static class CacheComponentHazelcastConfigurer implements HazelcastConfigurer {
        @Override
        public void configure(final Config config) {
            final Map<String, CacheSimpleConfig> cacheConfigs = new HashMap<>();
            cacheConfigs.put("test_cache", new CacheSimpleConfig().setStatisticsEnabled(true));
            config.setCacheConfigs(cacheConfigs);
        }
    }

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private ICache<Object, Object> cache;
    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    private void init() {
        cache = hazelcastInstance.getCacheManager().getCache("test_cache");
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
            // Put
            cache.put(rand.nextInt(20), rand.nextInt(100));

            // Get
            cache.get(rand.nextInt(20));

            // Remove
            cache.remove(rand.nextInt(20));
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the cache", e);
            scheduledFuture.cancel(true);
        }
    }
}
