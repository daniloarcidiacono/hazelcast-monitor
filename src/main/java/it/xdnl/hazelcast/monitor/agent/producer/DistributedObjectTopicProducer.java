package it.xdnl.hazelcast.monitor.agent.producer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.cache.ICache;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.*;
import com.hazelcast.ringbuffer.Ringbuffer;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import it.xdnl.hazelcast.monitor.agent.exception.UpdateParameterException;
import it.xdnl.hazelcast.monitor.agent.filter.ICollectionFilter;
import it.xdnl.hazelcast.monitor.agent.filter.IFilter;
import it.xdnl.hazelcast.monitor.agent.helper.FilterRegistry;
import it.xdnl.hazelcast.monitor.agent.product.ListProduct;
import it.xdnl.hazelcast.monitor.agent.product.MapProduct;
import it.xdnl.hazelcast.monitor.agent.product.Product;

import javax.cache.Cache;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Producer that iterates on Hazelcast's distributed objects picking only type of {@code distributedObjectType}.
 */
public class DistributedObjectTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "distributed_object_details";
    private static final ObjectMapper mapper = new ObjectMapper();
    private DistributedObjectType distributedObjectType;
    private String objectName;
    private HazelcastInstance instance;
    private FilterRegistry filterRegistry;
    private String filterName;

    static {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public DistributedObjectTopicProducer(final String instanceName,
                                          final DistributedObjectType distributedObjectType,
                                          final String objectName,
                                          final FilterRegistry filterRegistry) {
        super(TOPIC_TYPE);

        this.distributedObjectType = distributedObjectType;
        this.objectName = objectName;
        this.filterRegistry = filterRegistry;
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public void updateParameter(final String parameter, final String value) throws UpdateParameterException {
        if (parameter.equals("filter")) {
            filterName = value;
        } else {
            super.updateParameter(parameter, value);
        }
    }

    @Override
    public Product produce() {
        switch (distributedObjectType) {
            case MAP: {
                return produceMap();
            }

            case LIST: {
                return produceList();
            }

            case MULTIMAP: {
                return produceMultiMap();
            }

            case QUEUE: {
               return produceQueue();
            }

            case REPLICATEDMAP: {
                return produceReplicatedMap();
            }

            case SET: {
                return produceSet();
            }

            // @TODO: Ringbuffers are problematic to read
//            case RINGBUFFER: {
//                return produceRingbuffer();
//            }

            case CACHE: {
                return produceCache();
            }
        }

        return null;
    }

    private MapProduct produceMap() {
        final MapProduct product = new MapProduct();
        final IMap map = instance.getMap(objectName);
        final Set<Map.Entry> entries = map.entrySet();
        for (Map.Entry entry : entries) {
            product.add(
                new MapProduct.Entry(
                    mapper.valueToTree(entry.getKey()),
                    mapper.valueToTree(entry.getValue()),
                    entry.getKey().toString(),
                    entry.getValue().toString(),
                    map.isLocked(entry.getKey())
                )
            );
        }

        return product;
    }

    private ListProduct produceList() {
        final ListProduct product = new ListProduct();
        final IList list = instance.getList(objectName);
        final IFilter filter = filterName != null ? filterRegistry.getFilterByName(filterName) : null;

        for (Object entry : list) {
            // Filtering
            if (filter instanceof ICollectionFilter) {
                if (!((ICollectionFilter)filter).matches(entry)) {
                    continue;
                }
            }

            product.add(
                new ListProduct.Entry(
                    mapper.valueToTree(entry),
                    entry.toString()
                )
            );
        }

        return product;
    }

    private MapProduct produceMultiMap() {
        final MapProduct product = new MapProduct();
        final MultiMap map = instance.getMultiMap(objectName);
        final Set<Object> keys = map.keySet();
        for (Object key : keys) {
            final Collection<Object> values = map.get(key);
            product.add(
                new MapProduct.Entry(
                    mapper.valueToTree(key),
                    mapper.valueToTree(values),
                    key.toString(),
                    "Collection",
                    map.isLocked(key)
                )
            );
        }

        return product;
    }

    private ListProduct produceQueue() {
        final ListProduct product = new ListProduct();
        final IQueue queue = instance.getQueue(objectName);
        for (Object entry : queue) {
            product.add(
                new ListProduct.Entry(
                    mapper.valueToTree(entry),
                    entry.toString()
                )
            );
        }

        return product;
    }

    private ListProduct produceSet() {
        final ListProduct product = new ListProduct();
        final ISet set = instance.getSet(objectName);
        for (Object entry : set) {
            product.add(
                new ListProduct.Entry(
                    mapper.valueToTree(entry),
                    entry.toString()
                )
            );
        }

        return product;
    }

    private MapProduct produceReplicatedMap() {
        final MapProduct product = new MapProduct();
        final ReplicatedMap map = instance.getReplicatedMap(objectName);
        final Set<Map.Entry> entries = map.entrySet();
        for (Map.Entry entry : entries) {
            product.add(
                new MapProduct.Entry(
                    mapper.valueToTree(entry.getKey()),
                    mapper.valueToTree(entry.getValue()),
                    entry.getKey().toString(),
                    entry.getValue().toString(),
                    false
                )
            );
        }

        return product;
    }

    private MapProduct produceCache() {
        final MapProduct product = new MapProduct();
        final ICache cache = instance.getCacheManager().getCache(objectName);
        final Iterator<Cache.Entry> iterator = cache.iterator();
        while (iterator.hasNext()) {
            final Cache.Entry entry = iterator.next();
            product.add(
                new MapProduct.Entry(
                    mapper.valueToTree(entry.getKey()),
                    mapper.valueToTree(entry.getValue()),
                    entry.getKey().toString(),
                    entry.getValue().toString(),
                    false
                )
            );
        }

        return product;
    }
}
