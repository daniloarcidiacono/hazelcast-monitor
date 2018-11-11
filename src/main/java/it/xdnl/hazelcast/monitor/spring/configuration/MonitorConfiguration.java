package it.xdnl.hazelcast.monitor.spring.configuration;

import com.hazelcast.core.HazelcastInstance;
import it.xdnl.hazelcast.monitor.agent.HazelcastAgent;
import it.xdnl.hazelcast.monitor.agent.factory.TopicProducerFactory;
import it.xdnl.hazelcast.monitor.agent.handler.MessageHandler;
import it.xdnl.hazelcast.monitor.agent.handler.SubscribeMessageHandler;
import it.xdnl.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import it.xdnl.hazelcast.monitor.agent.query.PredicateQueryEngine;
import it.xdnl.hazelcast.monitor.spring.property.MonitorThreadsProperties;
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

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Bean
    public ConnectionSubscriptionsRegistry connectionSubscriptionsRegistry() {
        return new ConnectionSubscriptionsRegistry();
    }

    @Bean
    public PredicateQueryEngine predicateQueryEngine() {
        return new PredicateQueryEngine(hazelcastInstance);
    }

    @Bean
    public TopicProducerFactory topicFactory() {
        final TopicProducerFactory topicProducerFactory = new TopicProducerFactory(threadsProperties.getThreadPoolSize());
        topicProducerFactory.setConnectionSubscriptionsRegistry(connectionSubscriptionsRegistry());
        topicProducerFactory.setPredicateQueryEngine(predicateQueryEngine());

        return topicProducerFactory;
    }

    @Bean
    public MessageHandler subscribeMessageHandler() {
        final SubscribeMessageHandler subscribeMessageHandler = new SubscribeMessageHandler();
        subscribeMessageHandler.setTopicProducerFactory(topicFactory());
        subscribeMessageHandler.setSubscriptionsRegistry(connectionSubscriptionsRegistry());

        return subscribeMessageHandler;
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
