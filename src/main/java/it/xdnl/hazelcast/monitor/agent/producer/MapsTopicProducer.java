package it.xdnl.hazelcast.monitor.agent.producer;

import com.hazelcast.core.*;
import it.xdnl.hazelcast.monitor.agent.product.MapsProduct;
import it.xdnl.hazelcast.monitor.agent.product.MembersProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "maps";
    private static final Logger logger = LoggerFactory.getLogger(MapsTopicProducer.class);
    private HazelcastInstance instance;

    public MapsTopicProducer(final String instanceName) {
        super(TOPIC_TYPE);
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
    }

    @Override
    public MapsProduct produce() {
        try {
            final MapsProduct product = new MapsProduct();
            for (DistributedObject object : instance.getDistributedObjects()) {
                if (object instanceof IMap) {
                    final IMap map = (IMap)object;
                    product.getMaps().add(
                        new MapsProduct.MapSummaryProduct(
                            object.getName(),
                            map.size()
                        )
                    );
                }
            }

            return product;
        } catch (Exception e) {
            logger.error("Error", e);
            return null;
        }
    }
}
