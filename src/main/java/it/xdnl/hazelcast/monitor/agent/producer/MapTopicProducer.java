package it.xdnl.hazelcast.monitor.agent.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import it.xdnl.hazelcast.monitor.agent.product.MapProduct;

import java.util.Map;
import java.util.Set;

public class MapTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "map";
    private static final ObjectMapper mapper = new ObjectMapper();
    private HazelcastInstance instance;
    private String mapName;

    public MapTopicProducer(final String instanceName, final String mapName) {
        super(TOPIC_TYPE);
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
        this.mapName = mapName;
    }

    @Override
    public MapProduct produce() {
        final MapProduct product = new MapProduct();
        final IMap map = instance.getMap(mapName);
        final Set<Map.Entry> entries = map.entrySet();
        for (Map.Entry entry : entries) {
            product.add(
                new MapProduct.Entry(
                    mapper.valueToTree(entry.getKey()),
                    mapper.valueToTree(entry.getValue()),
                    entry.getKey().toString(),
                    entry.getValue().toString()
                )
            );
        }

        return product;
    }
}
