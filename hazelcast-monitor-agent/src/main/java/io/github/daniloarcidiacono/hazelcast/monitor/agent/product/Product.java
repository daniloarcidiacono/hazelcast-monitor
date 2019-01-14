package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField;

@TypescriptDTO
public abstract class Product {
    /**
     * Time in milliseconds that took to generate the product.
     */
    @TypescriptField(required = false)
    private Long produceTime;

    public Product() {
    }

    public Long getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Long produceTime) {
        this.produceTime = produceTime;
    }
}
