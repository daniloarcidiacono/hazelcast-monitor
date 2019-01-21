package io.github.daniloarcidiacono.hazelcast.monitor.sample.app;

import io.github.daniloarcidiacono.hazelcast.monitor.starter.spring.annotation.EnableHazelcastMonitor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHazelcastMonitor
public class HazelcastMonitorTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(HazelcastMonitorTestApplication.class, args);
    }
}
