package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.configuration;

import com.hazelcast.config.*;
import io.github.daniloarcidiacono.hazelcast.monitor.sample.app.property.SampleAppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class HazelcastConfiguration {
    @Autowired
    private SampleAppProperties appProperties;

    @Autowired(required = false)
    private List<HazelcastConfigurer> hazelcastConfigurerList = new ArrayList<>();

    @Bean
    public Config config() {
        final Config config = new Config();
        config.setManagementCenterConfig(
            new ManagementCenterConfig()
                .setUrl(appProperties.getManagementCenterUrl())
                .setEnabled(appProperties.isManagementCenterEnabled())
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
                .setName(appProperties.getGroupName())
                .setPassword(appProperties.getGroupPassword())
        )
        .setInstanceName(appProperties.getInstanceName());

        // Apply configurers
        for (HazelcastConfigurer hazelcastConfigurer : hazelcastConfigurerList) {
            hazelcastConfigurer.configure(config);
        }

        return config;
    }
}
