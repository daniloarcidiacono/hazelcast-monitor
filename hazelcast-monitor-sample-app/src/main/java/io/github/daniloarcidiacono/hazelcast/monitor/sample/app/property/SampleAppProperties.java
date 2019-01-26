package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("sampleapp")
public class SampleAppProperties {
    @NestedConfigurationProperty
    private Map<String, HazelcastInstanceProperties> instances = new HashMap<>();

    public Map<String, HazelcastInstanceProperties> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, HazelcastInstanceProperties> instances) {
        this.instances = instances;
    }
}
