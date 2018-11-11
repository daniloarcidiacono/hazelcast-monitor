package it.xdnl.hazelcast.monitor.app;

import it.xdnl.hazelcast.monitor.spring.annotation.EnableHazelcastMonitor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHazelcastMonitor
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
