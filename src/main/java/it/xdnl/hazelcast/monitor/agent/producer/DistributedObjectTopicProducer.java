package it.xdnl.hazelcast.monitor.agent.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.*;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectTopic;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectType;
import it.xdnl.hazelcast.monitor.agent.dto.topic.DistributedObjectsTopic;
import it.xdnl.hazelcast.monitor.agent.product.*;

import java.util.Collection;
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

    public DistributedObjectTopicProducer(final String instanceName, final DistributedObjectType distributedObjectType, final String objectName) {
        super(TOPIC_TYPE);
        this.distributedObjectType = distributedObjectType;
        this.objectName = objectName;
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
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
        for (Object entry : list) {
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
}
