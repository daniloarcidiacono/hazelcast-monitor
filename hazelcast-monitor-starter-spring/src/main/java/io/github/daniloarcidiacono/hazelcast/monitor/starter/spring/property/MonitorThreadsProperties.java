package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.property;

import io.github.daniloarcidiacono.hazelcast.monitor.agent.factory.TopicProducerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Threads properties.
 * @see MonitorProperties
 */
@Configuration
@ConfigurationProperties("monitor.threads")
public class MonitorThreadsProperties {
    /**
     * Size of the thread pool used by {@link TopicProducerFactory} to
     * generate periodic data.
     */
    private int threadPoolSize;

    public MonitorThreadsProperties() {
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
}