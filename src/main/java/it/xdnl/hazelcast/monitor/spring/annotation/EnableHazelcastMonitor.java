package it.xdnl.hazelcast.monitor.spring.annotation;

import it.xdnl.hazelcast.monitor.spring.configuration.HazelcastMonitorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(HazelcastMonitorConfiguration.class)
public @interface EnableHazelcastMonitor {
}