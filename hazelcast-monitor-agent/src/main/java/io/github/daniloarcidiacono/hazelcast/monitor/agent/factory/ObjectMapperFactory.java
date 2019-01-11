package io.github.daniloarcidiacono.hazelcast.monitor.agent.factory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory for centralized {@link ObjectMapper} instancing.
 * This is useful when the host application has custom serializers that must be used by
 * the monitor for producing the responses.
 */
public interface ObjectMapperFactory {
    ObjectMapper instance();
}
