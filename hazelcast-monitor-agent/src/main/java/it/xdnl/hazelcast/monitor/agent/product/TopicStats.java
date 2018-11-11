package it.xdnl.hazelcast.monitor.agent.product;

import com.hazelcast.monitor.LocalTopicStats;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.io.Serializable;
import java.util.Collection;

@TypescriptDTO
public class TopicStats implements Serializable {
    @TypescriptComments("Total number of received messages of this topic on this member.")
    private long receiveOperationCount = 0;

    @TypescriptComments("Total number of published messages of this topic on this member.")
    private long publishOperationCount = 0;

    @TypescriptComments("Creation time of this topic on this member.")
    private long creationTime = 0;

    public TopicStats() {
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
        final TopicStats result = new TopicStats();
        result.setReceiveOperationCount(localTopicStats.getReceiveOperationCount());
        result.setPublishOperationCount(localTopicStats.getPublishOperationCount());
        result.setCreationTime(localTopicStats.getCreationTime());

        return result;
    }

    public long getReceiveOperationCount() {
        return receiveOperationCount;
    }

    public TopicStats setReceiveOperationCount(long receiveOperationCount) {
        this.receiveOperationCount = receiveOperationCount;
        return this;
    }

    public long getPublishOperationCount() {
        return publishOperationCount;
    }

    public TopicStats setPublishOperationCount(long publishOperationCount) {
        this.publishOperationCount = publishOperationCount;
        return this;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public TopicStats setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }
}
