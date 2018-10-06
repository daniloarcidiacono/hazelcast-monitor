package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.agent.helper.EnvironmentVariable;
import it.xdnl.hazelcast.monitor.agent.helper.SubscriptionRegistryStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TypescriptDTO
public class InternalsProduct implements Product {
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
