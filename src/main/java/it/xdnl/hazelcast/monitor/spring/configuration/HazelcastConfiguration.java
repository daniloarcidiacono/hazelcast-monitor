package it.xdnl.hazelcast.monitor.spring.configuration;

import com.hazelcast.config.*;
import it.xdnl.chat.typescript.TypescriptParser;
import it.xdnl.chat.typescript.TypescriptRenderer;
import it.xdnl.chat.typescript.declaration.TypescriptDeclaration;
import it.xdnl.hazelcast.monitor.agent.dto.AbstractMessage;
import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class HazelcastConfiguration {
    @Bean
    public Config config() {
        final Config config = new Config();
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
