package io.github.daniloarcidiacono.hazelcast.monitor.agent.producer;

import com.hazelcast.config.Config;
import com.hazelcast.config.ConfigXmlGenerator;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.helper.EnvironmentVariable;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.product.InternalsProduct;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
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
    private HazelcastInstance instance;
    private final ConfigXmlGenerator configXmlGenerator = new ConfigXmlGenerator(true);

    public InternalsTopicProducer(final String instanceName, final ConnectionSubscriptionsRegistry registry) {
        super(TOPIC_TYPE, instanceName);
        this.registry = registry;
        instance = Hazelcast.getHazelcastInstanceByName(instanceName);
        for (Map.Entry<String, String> variable : System.getenv().entrySet()) {
            envVariables.add(new EnvironmentVariable(variable.getKey(), variable.getValue()));
        }
    }

    @Override
    public InternalsProduct produce() {
        final InternalsProduct product = new InternalsProduct();
        product.setSubscriptionStats(registry.getLocalStatistics());
        product.setEnvVariables(envVariables);

        final Config config = instance.getConfig();
        final String configXmlString = configXmlGenerator.generate(config);
        product.setMemberConfig(configXmlString);

        return product;
    }
}
