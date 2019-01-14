package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.helper.EnvironmentVariable;
import io.github.daniloarcidiacono.hazelcast.monitor.agent.helper.SubscriptionRegistryStatistics;

import java.util.List;

@TypescriptDTO
public class InternalsProduct extends Product {
    private SubscriptionRegistryStatistics subscriptionStats;
    private List<EnvironmentVariable> envVariables;
    private String memberConfig;

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

    public String getMemberConfig() {
        return memberConfig;
    }

    public void setMemberConfig(String memberConfig) {
        this.memberConfig = memberConfig;
    }
}
