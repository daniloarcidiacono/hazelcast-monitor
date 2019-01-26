package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.configuration;

import com.hazelcast.config.Config;

/**
 * Configurer for Hazelcast {@link Config} object.
 * @see HazelcastAppFactory
 */
public interface HazelcastConfigurer {
    /**
     * Applies additional configuration to Hazelcast.
     * @param config the configuration object
     */
    void configure(final Config config);
}
