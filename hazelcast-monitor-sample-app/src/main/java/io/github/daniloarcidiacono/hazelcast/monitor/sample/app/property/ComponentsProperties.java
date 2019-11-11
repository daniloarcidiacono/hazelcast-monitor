package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sampleapp.components")
public class ComponentsProperties {
    /**
     * Size of the thread pool available to the components
     */
    private int threadPoolSize = 16;

    /**
     * Frequency (events per minute) of every component that runs as a poisson process.
     */
    private int frequency = 60;

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
