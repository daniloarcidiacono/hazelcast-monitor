package it.xdnl.hazelcast.monitor.agent.product;

import com.hazelcast.monitor.LocalTopicStats;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.io.Serializable;
import java.util.Collection;

@TypescriptDTO
public class TopicStats implements Serializable {
    private long receiveOperationCount = 0;
    private long publishOperationCount = 0;
    private long creationTime = 0;

    public TopicStats() {
    }

    public TopicStats(long receiveOperationCount, long publishOperationCount, long creationTime) {
        this.receiveOperationCount = receiveOperationCount;
        this.publishOperationCount = publishOperationCount;
        this.creationTime = creationTime;
    }

    public static TopicStats aggregated(final Collection<TopicStats> stats) {
        final int statCount = stats.size();
        final TopicStats result = new TopicStats();
        for (TopicStats stat : stats) {
            result.receiveOperationCount += stat.receiveOperationCount;
            result.publishOperationCount += stat.publishOperationCount;
            result.creationTime += stat.creationTime / statCount;
        }

        return result;
    }

    public static TopicStats fromHazelcast(final LocalTopicStats localTopicStats) {
        return new TopicStats(
                localTopicStats.getReceiveOperationCount(),
                localTopicStats.getPublishOperationCount(),
                localTopicStats.getCreationTime()
        );
    }

    public long getReceiveOperationCount() {
        return receiveOperationCount;
    }

    public void setReceiveOperationCount(long receiveOperationCount) {
        this.receiveOperationCount = receiveOperationCount;
    }

    public long getPublishOperationCount() {
        return publishOperationCount;
    }

    public void setPublishOperationCount(long publishOperationCount) {
        this.publishOperationCount = publishOperationCount;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
