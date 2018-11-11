package it.xdnl.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.HashMap;
import java.util.Map;

@TypescriptDTO
public class StatsProduct<T> extends Product {
    private long sampleTime;
    private Map<String, T> members = new HashMap<>();
    private T aggregated;

    public StatsProduct() {
    }


    public void add(final String member, final T stat) {
        members.put(member, stat);
    }

    public Map<String, T> getMembers() {
        return members;
    }

    public void setMembers(Map<String, T> members) {
        this.members = members;
    }

    public T getAggregated() {
        return aggregated;
    }

    public void setAggregated(T aggregated) {
        this.aggregated = aggregated;
    }

    public long getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(long sampleTime) {
        this.sampleTime = sampleTime;
    }
}
