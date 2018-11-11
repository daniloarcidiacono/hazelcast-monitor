package it.xdnl.hazelcast.monitor.agent.query;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import it.xdnl.hazelcast.monitor.agent.utils.PredicateUtils;

import javax.cache.Cache;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Searches in a {@link ICache} using a {@link Predicate}.
 *
 * @param <K>
 * @param <v>
 * @see PredicateQueryEngine#queryCache(ICache, Predicate)
 */
class CacheQueryTask<K, V> implements Callable<List<Cache.Entry<K, V>>>, DataSerializable, HazelcastInstanceAware {
    private HazelcastInstance hazelcastInstance;
    private String cacheName;
    private Predicate predicate;

    public CacheQueryTask() {
    }

    public CacheQueryTask(final String cacheName, final Predicate predicate) {
        this.cacheName = cacheName;
        this.predicate = predicate;
    }

    @Override
    public List<Cache.Entry<K, V>> call() throws Exception {
        if (predicate instanceof ScriptPredicate) {
            ((ScriptPredicate)predicate).prepare();
        }

        final List<Cache.Entry<K, V>> result = new ArrayList<>();
        final ICache<K, V> cache = hazelcastInstance.getCacheManager().getCache(cacheName);
        final Iterator<Cache.Entry<K, V>> iterator = cache.iterator(cache.size());

        while (iterator.hasNext()) {
            final Cache.Entry cacheEntry = iterator.next();
            final SimpleEntry simpleEntry = new SimpleEntry(cacheEntry.getKey(), cacheEntry.getValue());
            if (PredicateUtils.safePredicateApply(predicate, simpleEntry)) {
                result.add(cacheEntry);
            }
        }

        return result;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(cacheName);
        out.writeObject(predicate);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        cacheName = in.readUTF();
        predicate = in.readObject();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
