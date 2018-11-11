package it.xdnl.hazelcast.monitor.spring.annotation;

import it.xdnl.hazelcast.monitor.spring.configuration.MonitorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(MonitorConfiguration.class)
public @interface EnableHazelcastMonitor {
}