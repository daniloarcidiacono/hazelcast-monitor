package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import com.hazelcast.monitor.LocalQueueStats;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField;

import java.io.Serializable;
import java.util.Collection;

@TypescriptDTO
public class QueueStats implements Serializable {
    @TypescriptComments("Creation time on this member.")
    private long creationTime = 0;

    @TypescriptComments("Number of owned items in this member.")
    private long ownedItemCount = 0;

    @TypescriptComments("Number of backup items in this member.")
    private long backupItemCount = 0;

    @TypescriptComments("Minimum age of the items in this member.")
    @TypescriptField(required = false)
    private Long minAge;

    @TypescriptComments("Maximum age of the items in this member.")
    @TypescriptField(required = false)
    private Long maxAge;

    @TypescriptComments("Average age of the items in this member.")
    private long avgAge = 0;

    @TypescriptComments({
        "Number of offer/put/add operations.",
        "Offers returning false will be included."
    })
    private long offerOperationCount = 0;

    @TypescriptComments({
        "Number of rejected offers.",
        "Offer can be rejected because of max-size limit on the queue."
    })
    private long rejectedOfferOperationCount = 0;

    @TypescriptComments({
         "Number of poll/take/remove operations.",
         "Polls returning null (empty) will be included."
    })
    private long pollOperationCount = 0;

    @TypescriptComments({
        "Number of null returning poll operations.",
        "Poll operation might return null, if the queue is empty."
    })
    private long emptyPollOperationCount = 0;

    @TypescriptComments("Number of other operations")
    private long otherOperationsCount = 0;

    @TypescriptComments("Number of event operations")
    private long eventOperationCount = 0;

    public QueueStats() {
    }

    public static QueueStats aggregated(final Collection<QueueStats> stats) {
        final int statCount = stats.size();
        final QueueStats result = new QueueStats();
        for (QueueStats stat : stats) {
            result.creationTime += stat.creationTime / statCount;

            if (result.minAge == null) {
                result.minAge = stat.minAge;
            } else if (stat.minAge != null) {
                result.minAge = Math.min(result.minAge, stat.minAge);
            }

            if (result.maxAge == null) {
                result.maxAge = stat.maxAge;
            } else if (stat.maxAge != null) {
                result.maxAge = Math.max(result.minAge, stat.maxAge);
            }

            result.avgAge += stat.avgAge / statCount;
            result.ownedItemCount += stat.ownedItemCount;
            result.backupItemCount += stat.backupItemCount;
            result.offerOperationCount += stat.offerOperationCount;
            result.rejectedOfferOperationCount += stat.rejectedOfferOperationCount;
            result.pollOperationCount += stat.pollOperationCount;
            result.emptyPollOperationCount += stat.emptyPollOperationCount;
            result.otherOperationsCount += stat.otherOperationsCount;
            result.eventOperationCount += stat.eventOperationCount;
        }

        return result;
    }

    public static QueueStats fromHazelcast(final LocalQueueStats localQueueStats) {
        final QueueStats result = new QueueStats();
        result.setCreationTime(localQueueStats.getCreationTime());
        result.setOwnedItemCount(localQueueStats.getOwnedItemCount());
        result.setBackupItemCount(localQueueStats.getBackupItemCount());
        result.setMinAge(localQueueStats.getMinAge());
        result.setMaxAge(localQueueStats.getMaxAge());
        result.setAvgAge(localQueueStats.getAvgAge());
        result.setOfferOperationCount(localQueueStats.getOfferOperationCount());
        result.setRejectedOfferOperationCount(localQueueStats.getRejectedOfferOperationCount());
        result.setPollOperationCount(localQueueStats.getPollOperationCount());
        result.setEmptyPollOperationCount(localQueueStats.getEmptyPollOperationCount());
        result.setOtherOperationsCount(localQueueStats.getOtherOperationsCount());
        result.setEventOperationCount(localQueueStats.getEventOperationCount());

        // Weird but correct
        if (result.getMinAge() == Long.MAX_VALUE) {
            result.setMinAge(null);
        }

        if (result.getMaxAge() == Long.MIN_VALUE) {
            result.setMaxAge(null);
        }

        return result;
    }

    public long getOwnedItemCount() {
        return ownedItemCount;
    }

    public QueueStats setOwnedItemCount(long ownedItemCount) {
        this.ownedItemCount = ownedItemCount;
        return this;
    }

    public long getBackupItemCount() {
        return backupItemCount;
    }

    public QueueStats setBackupItemCount(long backupItemCount) {
        this.backupItemCount = backupItemCount;
        return this;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public QueueStats setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Long getMinAge() {
        return minAge;
    }

    public QueueStats setMinAge(Long minAge) {
        this.minAge = minAge;
        return this;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public QueueStats setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public long getAvgAge() {
        return avgAge;
    }

    public QueueStats setAvgAge(long avgAge) {
        this.avgAge = avgAge;
        return this;
    }

    public long getOfferOperationCount() {
        return offerOperationCount;
    }

    public QueueStats setOfferOperationCount(long offerOperationCount) {
        this.offerOperationCount = offerOperationCount;
        return this;
    }

    public long getRejectedOfferOperationCount() {
        return rejectedOfferOperationCount;
    }

    public QueueStats setRejectedOfferOperationCount(long rejectedOfferOperationCount) {
        this.rejectedOfferOperationCount = rejectedOfferOperationCount;
        return this;
    }

    public long getPollOperationCount() {
        return pollOperationCount;
    }

    public QueueStats setPollOperationCount(long pollOperationCount) {
        this.pollOperationCount = pollOperationCount;
        return this;
    }

    public long getEmptyPollOperationCount() {
        return emptyPollOperationCount;
    }

    public QueueStats setEmptyPollOperationCount(long emptyPollOperationCount) {
        this.emptyPollOperationCount = emptyPollOperationCount;
        return this;
    }

    public long getOtherOperationsCount() {
        return otherOperationsCount;
    }

    public QueueStats setOtherOperationsCount(long otherOperationsCount) {
        this.otherOperationsCount = otherOperationsCount;
        return this;
    }

    public long getEventOperationCount() {
        return eventOperationCount;
    }

    public QueueStats setEventOperationCount(long eventOperationCount) {
        this.eventOperationCount = eventOperationCount;
        return this;
    }
}
