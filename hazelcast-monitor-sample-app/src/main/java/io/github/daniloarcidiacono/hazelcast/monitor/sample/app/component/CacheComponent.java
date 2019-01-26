package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.cache.ICache;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.configuration.HazelcastConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class CacheComponent implements Runnable {
    public static class CacheComponentHazelcastConfigurer implements HazelcastConfigurer {
        public void configure(final Config config) {
            final Map<String, CacheSimpleConfig> cacheConfigs = new HashMap<>();
            cacheConfigs.put("test_cache", new CacheSimpleConfig().setStatisticsEnabled(true));
            config.setCacheConfigs(cacheConfigs);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(CacheComponent.class);
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
    private final Random rand = new Random();
    private final HazelcastInstance hazelcastInstance;
    private ICache<Object, Object> cache;
    private ScheduledFuture<?> scheduledFuture;

    public CacheComponent(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        cache = hazelcastInstance.getCacheManager().getCache("test_cache");
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
