package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.configuration;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.property.HazelcastInstanceProperties;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.property.SampleAppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for {@link HazelcastApp} objects.
 *
 * @see SampleAppProperties
 * @see HazelcastApp
 */
@Component
public class HazelcastAppFactory {
    private static final Logger logger = LoggerFactory.getLogger(HazelcastAppFactory.class);

    @Autowired
    private SampleAppProperties properties;

    private Map<String, HazelcastApp> apps = new HashMap<>();

    @PostConstruct
    public void init() {
        for (Map.Entry<String, HazelcastInstanceProperties> entry : properties.getInstances().entrySet()) {
            logger.info("Creating instance {}", entry.getKey());

            // Create the config
            final Config config = createConfig(entry.getValue());

            // Customize the default configuration
            final HazelcastApp app = new HazelcastApp();
            app.configure(config);

            // Start the hazelcast instance
            final HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

            // Initialize and register the app
            app.create(instance);
            apps.put(entry.getKey(), app);
        }
    }

    @PreDestroy
    public void destroy() {
        for (HazelcastApp app : apps.values()) {
            app.destroy();
        }

        apps.clear();
    }

    private Config createConfig(final HazelcastInstanceProperties properties) {
        final Config config = new Config();
        config.setManagementCenterConfig(
            new ManagementCenterConfig()
                .setUrl(properties.getManagementCenterUrl())
                .setEnabled(properties.isManagementCenterEnabled())
        );

        config.setNetworkConfig(
            new NetworkConfig()
                .setPort(8000)
                .setPortAutoIncrement(true)
                .setJoin(
                    new JoinConfig()
                        .setMulticastConfig(new MulticastConfig().setEnabled(false))
                        .setTcpIpConfig(
                            new TcpIpConfig()
                                .setEnabled(true)
                                .setMembers(Collections.singletonList("127.0.0.1"))
                        )
                )
        )
        .setGroupConfig(
            new GroupConfig()
                .setName(properties.getGroupName())
                .setPassword(properties.getGroupPassword())
        )
        .setInstanceName(properties.getInstanceName());

        return config;
    }
}
