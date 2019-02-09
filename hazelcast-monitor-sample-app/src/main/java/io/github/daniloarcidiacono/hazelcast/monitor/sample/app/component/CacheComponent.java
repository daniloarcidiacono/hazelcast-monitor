package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.component;

import com.hazelcast.cache.ICache;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonExecutorService;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.utils.PoissonRunnableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test component for caches.
 * @see ICache
 */
public class CacheComponent implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CacheComponent.class);
    private final PoissonExecutorService executorService;
    private final Random rand = new Random();
    private ICache<Object, Object> cache;
    private PoissonRunnableWrapper poissonRunnable;

    public CacheComponent(final HazelcastInstance hazelcastInstance, final ScheduledExecutorService threadPool) {
        executorService = new PoissonExecutorService(threadPool);
        cache = hazelcastInstance.getCacheManager().getCache("test_cache");
        poissonRunnable = executorService.scheduleAsPoissonProcess(this, 30L, TimeUnit.MINUTES);
    }

    /**
     * Applies additional configuration to Hazelcast.
     * @param config the configuration object
     */
    public static void configure(final Config config) {
        final Map<String, CacheSimpleConfig> cacheConfigs = new HashMap<>();
        cacheConfigs.put("test_cache", new CacheSimpleConfig().setStatisticsEnabled(true));
        config.setCacheConfigs(cacheConfigs);
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
            // Put
            cache.put(rand.nextInt(20), rand.nextInt(100));

            // Get
            cache.get(rand.nextInt(20));

            // Remove
            cache.remove(rand.nextInt(20));
        } catch (HazelcastInstanceNotActiveException e){
            // This happens when killing the JVM, just stop
            poissonRunnable.stop();
        } catch (Exception e) {
            logger.error("Exception occurred when modifying the cache", e);
            poissonRunnable.stop();
        }
    }
}
