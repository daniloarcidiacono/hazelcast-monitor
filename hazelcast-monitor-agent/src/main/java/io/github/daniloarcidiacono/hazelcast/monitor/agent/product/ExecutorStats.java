package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import com.hazelcast.monitor.LocalExecutorStats;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.io.Serializable;
import java.util.Collection;

@TypescriptDTO
public class ExecutorStats implements Serializable {
    @TypescriptComments("Creation time on this member.")
    private long creationTime = 0;

    @TypescriptComments("Number of pending operations on the executor service")
    private long pendingTaskCount = 0;

    @TypescriptComments("Number of started operations on the executor service")
    private long startedTaskCount = 0;

    @TypescriptComments("Number of completed operations on the executor service")
    private long completedTaskCount = 0;

    @TypescriptComments("Number of cancelled operations on the executor service")
    private long cancelledTaskCount = 0;

    @TypescriptComments("Total start latency of operations started")
    private long totalStartLatency = 0;

    @TypescriptComments("Total execution time of operations finished")
    private long totalExecutionLatency = 0;

    public ExecutorStats() {
    }

    public static ExecutorStats aggregated(final Collection<ExecutorStats> stats) {
        final int statCount = stats.size();
        final ExecutorStats result = new ExecutorStats();
        for (ExecutorStats stat : stats) {
            result.creationTime += stat.creationTime / statCount;
            result.pendingTaskCount += stat.pendingTaskCount / statCount;
            result.startedTaskCount += stat.startedTaskCount / statCount;
            result.completedTaskCount += stat.completedTaskCount / statCount;
            result.cancelledTaskCount += stat.cancelledTaskCount / statCount;
            result.totalStartLatency += stat.totalStartLatency / statCount;
            result.totalExecutionLatency += stat.totalExecutionLatency / statCount;
        }

        return result;
    }

    public static ExecutorStats fromHazelcast(final LocalExecutorStats localExecutorStats) {
        final ExecutorStats result = new ExecutorStats();
        result.setCreationTime(localExecutorStats.getCreationTime());
        result.setPendingTaskCount(localExecutorStats.getPendingTaskCount());
        result.setStartedTaskCount(localExecutorStats.getStartedTaskCount());
        result.setCompletedTaskCount(localExecutorStats.getCompletedTaskCount());
        result.setCancelledTaskCount(localExecutorStats.getCancelledTaskCount());
        result.setTotalStartLatency(localExecutorStats.getTotalStartLatency());
        result.setTotalExecutionLatency(localExecutorStats.getTotalExecutionLatency());

        return result;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public ExecutorStats setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public long getPendingTaskCount() {
        return pendingTaskCount;
    }

    public ExecutorStats setPendingTaskCount(long pendingTaskCount) {
        this.pendingTaskCount = pendingTaskCount;
        return this;
    }

    public long getStartedTaskCount() {
        return startedTaskCount;
    }

    public ExecutorStats setStartedTaskCount(long startedTaskCount) {
        this.startedTaskCount = startedTaskCount;
        return this;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public ExecutorStats setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
        return this;
    }

    public long getCancelledTaskCount() {
        return cancelledTaskCount;
    }

    public ExecutorStats setCancelledTaskCount(long cancelledTaskCount) {
        this.cancelledTaskCount = cancelledTaskCount;
        return this;
    }

    public long getTotalStartLatency() {
        return totalStartLatency;
    }

    public ExecutorStats setTotalStartLatency(long totalStartLatency) {
        this.totalStartLatency = totalStartLatency;
        return this;
    }

    public long getTotalExecutionLatency() {
        return totalExecutionLatency;
    }

    public ExecutorStats setTotalExecutionLatency(long totalExecutionLatency) {
        this.totalExecutionLatency = totalExecutionLatency;
        return this;
    }
}
