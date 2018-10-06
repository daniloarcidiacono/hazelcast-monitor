package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import it.xdnl.hazelcast.monitor.agent.helper.EnvironmentVariable;
import it.xdnl.hazelcast.monitor.agent.helper.SubscriptionRegistryStatistics;

import java.util.List;

@TypescriptDTO
public class FiltersProduct implements Product {
    private List<String> filters;

    public FiltersProduct() {
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
}
