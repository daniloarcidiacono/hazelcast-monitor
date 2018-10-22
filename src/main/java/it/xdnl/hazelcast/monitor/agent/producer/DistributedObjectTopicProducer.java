package it.xdnl.hazelcast.monitor.agent.producer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.cache.ICache;
import com.hazelcast.core.*;
import com.jayway.jsonpath.JsonPath;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import it.xdnl.hazelcast.monitor.agent.exception.UpdateParameterException;
import it.xdnl.hazelcast.monitor.agent.product.ListProduct;
import it.xdnl.hazelcast.monitor.agent.product.MapProduct;
import it.xdnl.hazelcast.monitor.agent.product.Product;
import it.xdnl.hazelcast.monitor.agent.query.*;
import it.xdnl.hazelcast.monitor.agent.utils.JsonPathUtils;

import javax.cache.Cache;
import java.util.*;
import java.util.function.Predicate;

/**
 * Producer that iterates on Hazelcast's distributed objects picking only type of {@code distributedObjectType}.
 */
public class DistributedObjectTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "distributed_object_details";
    private static final ObjectMapper mapper = new ObjectMapper();
    private DistributedObjectType distributedObjectType;
    private String objectName;
    private HazelcastInstance instance;

    // Pagination
    private int page = 1;
    private int pageSize = 5;

    // Query
    private PredicateQueryEngine predicateQueryEngine;
    private Predicate predicate = TruePredicate.INSTANCE;
    private JsonPath jsonPath = null;

    static {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public DistributedObjectTopicProducer(final String instanceName,
                                          final DistributedObjectType distributedObjectType,
                                          final String objectName,
                                          final PredicateQueryEngine predicateQueryEngine) {
        super(TOPIC_TYPE);

        this.distributedObjectType = distributedObjectType;
        this.objectName = objectName;
        this.predicateQueryEngine = predicateQueryEngine;
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public void updateParameter(final String parameter, final String value) throws UpdateParameterException {
        switch (parameter) {
            case "filter":
                try {
                    predicate = new ScriptPredicate(value);
                } catch (Exception e) {
                    predicate = FalsePredicate.INSTANCE;
                    throw new UpdateParameterException("Error while updating the filter", e);
                }

                break;

            case "jsonPath":
                try {
                    jsonPath = JsonPathUtils.toJsonPath(value);
                } catch (Exception e) {
                    throw new UpdateParameterException("Error while updating the slice", e);
                }

                break;

            case "page":
                try {
                    final int newPage = Integer.parseInt(value);
                    page = newPage;
                    if (page < 1) {
                        page = 1;
                    }
                } catch (NumberFormatException e) {
                    final UpdateParameterException updateParameterException = new UpdateParameterException("Invalid page value: " + value);
                    updateParameterException.setParameterName(parameter);
                    updateParameterException.setActualValue(String.valueOf(page));

                    throw updateParameterException;
                }

                break;

            case "pageSize":
                final int newPageSize = Integer.parseInt(value);
                try {
                    pageSize = newPageSize;
                    if (pageSize < 1) {
                        pageSize = 1;
                    }

                } catch (NumberFormatException e) {
                    final UpdateParameterException updateParameterException = new UpdateParameterException("Invalid page size value: " + value);
                    updateParameterException.setParameterName(parameter);
                    updateParameterException.setActualValue(String.valueOf(pageSize));

                    throw updateParameterException;
                }

                break;

            default:
                super.updateParameter(parameter, value);
                break;
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
        try {
            final IMap map = instance.getMap(objectName);

            // Filter
            List<Map.Entry> filtered = predicateQueryEngine.queryMap(map, predicate);

            // Paginate
            final int start = pageSize * (page - 1);
            final int end = start + pageSize - 1;
            int current = start;
            while (current <= end && current < filtered.size()) {
                final Map.Entry entry = filtered.get(current);

                // Slice
                final Object sliced = JsonPathUtils.slice(entry.getValue(), jsonPath);

                // If we have applied the slice with success
                if (sliced != null) {
                    product.add(
                        new MapProduct.Entry(
                            mapper.valueToTree(entry.getKey()),
                            mapper.valueToTree(sliced),
                            entry.getKey().toString(),
                            sliced.toString(),
                            map.isLocked(entry.getKey())
                        )
                    );
                }

                current++;
            }
        } catch (PredicateQueryEngineException e) {
            predicate = FalsePredicate.INSTANCE;
        }

        return product;
    }

    private ListProduct produceList() {
        final ListProduct product = new ListProduct();
        try {
            final IList<Object> list = instance.getList(objectName);

            // Filter
            final List<Object> filtered = predicateQueryEngine.queryList(list, predicate);

            // Paginate
            final int start = pageSize * (page - 1);
            final int end = start + pageSize - 1;
            int current = start;
            while (current <= end && current < filtered.size()) {
                final Object entry = filtered.get(current);

                // Slice
                final Object sliced = JsonPathUtils.slice(entry, jsonPath);

                // If we have applied the slice with success
                if (sliced != null) {
                    product.add(
                        new ListProduct.Entry(
                            mapper.valueToTree(sliced),
                            sliced.toString()
                        )
                    );
                }

                current++;
            }
        } catch (PredicateQueryEngineException e) {
            predicate = FalsePredicate.INSTANCE;
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
        try {
            final IQueue queue = instance.getQueue(objectName);

            // Filter
            final List<Object> filtered = predicateQueryEngine.queryQueue(queue, predicate);

            // Paginate
            final int start = pageSize * (page - 1);
            final int end = start + pageSize - 1;
            int current = start;
            while (current <= end && current < filtered.size()) {
                final Object entry = filtered.get(current);

                // Slice
                final Object sliced = JsonPathUtils.slice(entry, jsonPath);

                // If we have applied the slice with success
                if (sliced != null) {
                    product.add(
                        new ListProduct.Entry(
                            mapper.valueToTree(sliced),
                            sliced.toString()
                        )
                    );
                }

                current++;
            }
        } catch (PredicateQueryEngineException e) {
            predicate = FalsePredicate.INSTANCE;
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
