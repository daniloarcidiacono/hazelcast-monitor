package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.configuration;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HazelcastConfiguration {
    @Bean
    public Config config() {
        final Config config = new Config();

        // Caches
        final Map<String, CacheSimpleConfig> cacheConfigs = new HashMap<>();
        cacheConfigs.put(
        "myCache",
            new CacheSimpleConfig()
                .setStatisticsEnabled(true)
        );
        config.setCacheConfigs(cacheConfigs);

        // Maps
        final Map<String, MapConfig> mapConfigs = new HashMap<>();
        mapConfigs.put(
            "myMap",
            new MapConfig()
                .setNearCacheConfig(
                    new NearCacheConfig()
                )
        );
        config.setMapConfigs(mapConfigs);

        config.setManagementCenterConfig(
            new ManagementCenterConfig()
                .setUrl("http://localhost:8080/mancenter")
                .setEnabled(false)
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
                                .setMembers(Arrays.asList("127.0.0.1")
                        )
                    )
                )
        )
        .setGroupConfig(
            new GroupConfig()
                .setName("user")
                .setPassword("pass")
        )
        .setInstanceName("dev");

        return config;
    }
}