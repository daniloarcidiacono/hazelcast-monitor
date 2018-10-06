package it.xdnl.hazelcast.monitor.agent.producer;

import it.xdnl.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import it.xdnl.hazelcast.monitor.agent.helper.EnvironmentVariable;
import it.xdnl.hazelcast.monitor.agent.product.InternalsProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InternalsTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "internals";
    private static final Logger logger = LoggerFactory.getLogger(InternalsTopicProducer.class);
    private ConnectionSubscriptionsRegistry registry;
    private List<EnvironmentVariable> envVariables = new ArrayList<>();

    public InternalsTopicProducer(final ConnectionSubscriptionsRegistry registry) {
        super(TOPIC_TYPE);
        this.registry = registry;
        for (Map.Entry<String, String> variable : System.getenv().entrySet()) {
            envVariables.add(new EnvironmentVariable(variable.getKey(), variable.getValue()));
        }
    }

    @Override
    public InternalsProduct produce() {
        final InternalsProduct product = new InternalsProduct();
        product.setSubscriptionStats(registry.getLocalStatistics());
        product.setEnvVariables(envVariables);

        return product;
    }
}
