package it.xdnl.hazelcast.monitor.spring.configuration;

import it.xdnl.hazelcast.monitor.agent.HazelcastAgent;
import it.xdnl.hazelcast.monitor.agent.factory.TopicProducerFactory;
import it.xdnl.hazelcast.monitor.agent.handler.MessageHandler;
import it.xdnl.hazelcast.monitor.agent.handler.SubscribeMessageHandler;
import it.xdnl.hazelcast.monitor.spring.property.MonitorThreadsProperties;
import it.xdnl.hazelcast.monitor.spring.property.MonitorWebSocketProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.List;

@Configuration
@ComponentScan(basePackages = { "it.xdnl.hazelcast.monitor.spring" })
@EnableWebSocket
@ConditionalOnMissingBean(HazelcastAgent.class)
public class MonitorConfiguration {
    @Autowired
    private MonitorThreadsProperties threadsProperties;

    @Bean
    public TopicProducerFactory topicFactory() {
        return new TopicProducerFactory(threadsProperties.getThreadPoolSize());
    }

    @Bean
    public MessageHandler subscribeMessageHandler(final TopicProducerFactory topicProducerFactory) {
        return new SubscribeMessageHandler(topicProducerFactory);
    }

    @Bean
    public HazelcastAgent hazelcastAgent(final List<MessageHandler> handlers) {
        final HazelcastAgent agent = new HazelcastAgent();
        for (MessageHandler handler : handlers) {
            agent.addHandler(handler);
        }

        return agent;
    }
}
