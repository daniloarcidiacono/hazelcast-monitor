package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class CardinalityEstimatorSummary extends DistributedObjectSummary {
    private long estimate;

    public CardinalityEstimatorSummary(final long estimate)  {
        this.estimate = estimate;
    }

    public long getEstimate() {
        return estimate;
    }

    public void setEstimate(long estimate) {
        this.estimate = estimate;
    }
}