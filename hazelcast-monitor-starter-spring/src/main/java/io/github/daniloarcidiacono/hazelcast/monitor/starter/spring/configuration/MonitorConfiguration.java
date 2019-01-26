package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.configuration;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.HazelcastAgent;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.DefaultObjectMapperFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.ObjectMapperFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.TopicProducerFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.handler.MessageHandler;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.handler.SubscribeMessageHandler;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.query.PredicateQueryEngine;
import io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.property.MonitorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.List;

@Configuration
@ComponentScan(basePackages = { "io.github.daniloarcidiacono.hazelcast.monitor.starter.spring" })
@EnableWebSocket
@ConditionalOnMissingBean(HazelcastAgent.class)
@ConditionalOnProperty(value = "monitor.enabled", havingValue = "true", matchIfMissing = true)
public class MonitorConfiguration {
    @Autowired
    private MonitorProperties monitorProperties;

    @Bean
    @ConditionalOnMissingBean
    public ConnectionSubscriptionsRegistry connectionSubscriptionsRegistry() {
        return new ConnectionSubscriptionsRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public PredicateQueryEngine predicateQueryEngine() {
        return new PredicateQueryEngine();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapperFactory objectMapperFactory() {
        return new DefaultObjectMapperFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public TopicProducerFactory topicFactory() {
        final TopicProducerFactory topicProducerFactory = new TopicProducerFactory(monitorProperties.getThreads().getThreadPoolSize());
        topicProducerFactory.setConnectionSubscriptionsRegistry(connectionSubscriptionsRegistry());
        topicProducerFactory.setPredicateQueryEngine(predicateQueryEngine());
        topicProducerFactory.setObjectMapperFactory(objectMapperFactory());

        return topicProducerFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageHandler subscribeMessageHandler() {
        final SubscribeMessageHandler subscribeMessageHandler = new SubscribeMessageHandler();
        subscribeMessageHandler.setTopicProducerFactory(topicFactory());
        subscribeMessageHandler.setSubscriptionsRegistry(connectionSubscriptionsRegistry());

        return subscribeMessageHandler;
    }

    @Bean
    public HazelcastAgent hazelcastAgent(final List<MessageHandler> handlers) {
        final HazelcastAgent agent = new HazelcastAgent();
        agent.setAuthenticationRequired(monitorProperties.isSecure());
        agent.setObjectMapperFactory(objectMapperFactory());
        for (MessageHandler handler : handlers) {
            agent.addHandler(handler);
        }

        return agent;
    }
}
