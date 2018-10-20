package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.agent.helper.EnvironmentVariable;
import it.xdnl.hazelcast.monitor.agent.helper.SubscriptionRegistryStatistics;

import java.util.List;

@TypescriptDTO
public class InternalsProduct extends Product {
    private SubscriptionRegistryStatistics subscriptionStats;
    private List<EnvironmentVariable> envVariables;

    public InternalsProduct() {
    }

    public SubscriptionRegistryStatistics getSubscriptionStats() {
        return subscriptionStats;
    }

    public void setSubscriptionStats(SubscriptionRegistryStatistics subscriptionStats) {
        this.subscriptionStats = subscriptionStats;
    }

    public List<EnvironmentVariable> getEnvVariables() {
        return envVariables;
    }

    public void setEnvVariables(List<EnvironmentVariable> envVariables) {
        this.envVariables = envVariables;
    }
}
