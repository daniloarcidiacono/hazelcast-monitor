package io.github.daniloarcidiacono.hazelcast.monitor.starter.javawebsockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.HazelcastAgent;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.DefaultObjectMapperFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.ObjectMapperFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.TopicProducerFactory;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.handler.MessageHandler;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.handler.SubscribeMessageHandler;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.helper.ConnectionSubscriptionsRegistry;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.query.PredicateQueryEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for {@link HazelcastAgent} class.
 */
public class HazelcastAgentBuilder {
    /**
     * Size of the thread pool used by {@link TopicProducerFactory} to
     * generate periodic data.
     */
    private int threadPoolSize = 4;

    /**
     * Whether the connection should be authenticated or not.
     */
    private boolean secure = true;

    /**
     * Additional handlers.
     */
    private List<MessageHandler> handlers = new ArrayList<>();

    /**
     * Factory for {@link ObjectMapper} objects.
     * <p>
     * It's useful to change this factory in order to customize the ObjectMapper used by the agent
     * (for example, when custom serialization is needed).
     */
    private ObjectMapperFactory objectMapperFactory = new DefaultObjectMapperFactory();

    /**
     * Builds an {@link HazelcastAgent} object.
     * @return the newly created instance.
     */
    public HazelcastAgent build() {
        final ConnectionSubscriptionsRegistry connectionSubscriptionsRegistry = new ConnectionSubscriptionsRegistry();
        final PredicateQueryEngine predicateQueryEngine = new PredicateQueryEngine();

        final TopicProducerFactory topicProducerFactory = new TopicProducerFactory(threadPoolSize);
        topicProducerFactory.setConnectionSubscriptionsRegistry(connectionSubscriptionsRegistry);
        topicProducerFactory.setPredicateQueryEngine(predicateQueryEngine);
        topicProducerFactory.setObjectMapperFactory(objectMapperFactory);

        final SubscribeMessageHandler subscribeMessageHandler = new SubscribeMessageHandler();
        subscribeMessageHandler.setTopicProducerFactory(topicProducerFactory);
        subscribeMessageHandler.setSubscriptionsRegistry(connectionSubscriptionsRegistry);

        final HazelcastAgent agent = new HazelcastAgent();
        agent.setAuthenticationRequired(secure);
        agent.setObjectMapperFactory(objectMapperFactory);
        agent.addHandler(subscribeMessageHandler);
        for (MessageHandler handler : handlers) {
            agent.addHandler(handler);
        }

        return agent;
    }

    public HazelcastAgentBuilder addHandler(final MessageHandler handler) {
        handlers.add(handler);
        return this;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public HazelcastAgentBuilder setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }

    public boolean isSecure() {
        return secure;
    }

    public HazelcastAgentBuilder setSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public List<MessageHandler> getHandlers() {
        return handlers;
    }

    public HazelcastAgentBuilder setHandlers(List<MessageHandler> handlers) {
        this.handlers = handlers;
        return this;
    }

    public ObjectMapperFactory getObjectMapperFactory() {
        return objectMapperFactory;
    }

    public HazelcastAgentBuilder setObjectMapperFactory(ObjectMapperFactory objectMapperFactory) {
        this.objectMapperFactory = objectMapperFactory;
        return this;
    }
}
