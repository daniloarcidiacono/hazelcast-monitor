package io.github.daniloarcidiacono.hazelcast.monitor.agent.factory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Default object mapper factory that simply instances an {@link ObjectMapper}.
 */
public class DefaultObjectMapperFactory implements ObjectMapperFactory {
    @Override
    public ObjectMapper instance() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        return mapper;
    }
}
