package it.xdnl.hazelcast.monitor.agent.producer;

import it.xdnl.hazelcast.monitor.agent.helper.FilterRegistry;
import it.xdnl.hazelcast.monitor.agent.product.FiltersProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FiltersTopicProducer extends AbstractTopicProducer {
    public static final String TOPIC_TYPE = "filters";
    private static final Logger logger = LoggerFactory.getLogger(FiltersTopicProducer.class);
    private FilterRegistry registry;

    public FiltersTopicProducer(final FilterRegistry registry) {
        super(TOPIC_TYPE);
        this.registry = registry;
    }

    @Override
    public FiltersProduct produce() {
        final List<String> filters = registry.getFilterNames();
        final FiltersProduct product = new FiltersProduct();
        product.setFilters(filters);

        return product;
    }
}
