package io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.annotation;

import io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.configuration.MonitorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables the Hazelcast Monitor.
 * 
 * @see MonitorConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(MonitorConfiguration.class)
public @interface EnableHazelcastMonitor {
}