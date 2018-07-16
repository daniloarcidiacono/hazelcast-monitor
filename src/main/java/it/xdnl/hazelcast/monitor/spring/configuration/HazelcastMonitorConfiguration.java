package it.xdnl.hazelcast.monitor.spring.configuration;

import it.xdnl.hazelcast.monitor.agent.HazelcastAgent;
import it.xdnl.hazelcast.monitor.agent.handler.ClustersMessageHandler;
import it.xdnl.hazelcast.monitor.agent.handler.SubscribeMessageHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Configuration
@ComponentScan(basePackages = { "it.xdnl.hazelcast.monitor.spring" })
@EnableWebSocket
public class HazelcastMonitorConfiguration {
    @Bean
    @ConditionalOnMissingBean(HazelcastAgent.class)
    public HazelcastAgent hazelcastAgent() {
        final HazelcastAgent agent = new HazelcastAgent();
        agent.addHandler(new ClustersMessageHandler());
        agent.addHandler(new SubscribeMessageHandler());

        return agent;
    }
}
