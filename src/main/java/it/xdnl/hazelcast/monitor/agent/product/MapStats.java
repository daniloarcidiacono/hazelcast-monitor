package it.xdnl.hazelcast.monitor.agent.product;

import com.hazelcast.monitor.LocalMapStats;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

@TypescriptDTO
public class MapStats implements Serializable {
    @TypescriptComments("Creation time of this map on this member")
    private long creationTime = 0;

    @TypescriptComments("Number of entries owned by this member")
    private long ownedEntryCount = 0;

    @TypescriptComments("Number of backup entries hold by this member")
    private long backupEntryCount = 0;

    @TypescriptComments("Number of backups per entry")
    private int backupCount = 0;

    @TypescriptComments("Memory cost (number of bytes) of owned entries in this member")
    private long ownedEntryMemoryCost = 0;

    @TypescriptComments("Memory cost (number of bytes) of backup entries in this member")
    private long backupEntryMemoryCost = 0;

    @TypescriptComments("Last access (read) time of the locally owned entries")
    private long lastAccessTime = Long.MIN_VALUE;

    @TypescriptComments("Last update time of the locally owned entries")
    private long lastUpdateTime = Long.MIN_VALUE;

    @TypescriptComments("Number of hits (reads) of the locally owned entries")
    private long hits = 0;

    @TypescriptComments("Number of currently locked locally owned keys")
    private long lockedEntryCount = 0;

    @TypescriptComments("Number of entries that the member owns and are dirty (updated but not persisted yet)")
    private long dirtyEntryCount = 0;

    @TypescriptComments("Number of put operations")
    private long putOperationCount = 0;

    @TypescriptComments("Number of get operations")
    private long getOperationCount = 0;

    @TypescriptComments("Number of Remove operations")
    private long removeOperationCount = 0;

    @TypescriptComments("Total latency of put operations. To get the average latency, divide by the number of puts")
    private long totalPutLatency = 0;

    @TypescriptComments("Total latency of get operations. To get the average latency, divide by the number of gets")
    private long totalGetLatency = 0;

    @TypescriptComments("Total latency of remove operations. To get the average latency, divide by the number of gets")
    private long totalRemoveLatency = 0;

    @TypescriptComments("Maximum latency of put operations")
    private long maxPutLatency = Long.MIN_VALUE;

    @TypescriptComments("Maximum latency of get operations")
    private long maxGetLatency = Long.MIN_VALUE;

    @TypescriptComments("Maximum latency of remove operations")
    private long maxRemoveLatency = Long.MIN_VALUE;

    @TypescriptComments("Number of Events Received")
    private long eventOperationCount = 0;

    @TypescriptComments("Total number of Other Operations")
    private long otherOperationCount = 0;

    @TypescriptComments("Total number of total operations")
    private long total = 0;

    @TypescriptComments("Cost of map & Near Cache & backup in bytes")
    private long heapCost = 0;

    @TypescriptComments("Statistics related to the Near Cache")
    private NearCacheStats nearCacheStatistics;

    public MapStats() {
    }

    public static MapStats aggregated(final Collection<MapStats> stats) {
        final int statCount = stats.size();
        final MapStats result = new MapStats();
        for (MapStats stat : stats) {
            result.creationTime += stat.creationTime / statCount;
            result.ownedEntryCount += stat.ownedEntryCount;
            result.backupEntryCount += stat.backupEntryCount;
            result.backupCount += stat.backupCount;
            result.ownedEntryMemoryCost += stat.ownedEntryMemoryCost;
            result.backupEntryMemoryCost += stat.backupEntryMemoryCost;
            result.lastAccessTime = Math.max(result.lastAccessTime, stat.lastAccessTime);
            result.lastUpdateTime = Math.max(result.lastUpdateTime, stat.lastUpdateTime);
            result.hits += stat.hits;
            result.lockedEntryCount += stat.lockedEntryCount;
            result.dirtyEntryCount += stat.dirtyEntryCount;
            result.putOperationCount += stat.putOperationCount;
            result.getOperationCount += stat.getOperationCount;
            result.removeOperationCount += stat.removeOperationCount;
            result.totalPutLatency += stat.totalPutLatency;
            result.totalGetLatency += stat.totalGetLatency;
            result.totalRemoveLatency += stat.totalRemoveLatency;
            result.maxPutLatency = Math.max(result.maxPutLatency, stat.maxPutLatency);
            result.maxGetLatency = Math.max(result.maxGetLatency, stat.maxGetLatency);
            result.maxRemoveLatency = Math.max(result.maxRemoveLatency, stat.maxRemoveLatency);
            result.eventOperationCount += stat.eventOperationCount;
            result.otherOperationCount += stat.otherOperationCount;
            result.total += stat.total;
            result.heapCost += stat.heapCost;
        }

        result.nearCacheStatistics = NearCacheStats.aggregated(
            stats.stream().map(MapStats::getNearCacheStatistics).collect(Collectors.toList())
        );

        return result;
    }

    public static MapStats fromHazelcast(final LocalMapStats localMapStats) {
        final MapStats result = new MapStats();
        result.setCreationTime(localMapStats.getCreationTime());
        result.setOwnedEntryCount(localMapStats.getOwnedEntryCount());
        result.setBackupEntryCount(localMapStats.getBackupEntryCount());
        result.setBackupCount(localMapStats.getBackupCount());
        result.setOwnedEntryMemoryCost(localMapStats.getOwnedEntryMemoryCost());
        result.setBackupEntryMemoryCost(localMapStats.getBackupEntryMemoryCost());
        result.setLastAccessTime(localMapStats.getLastAccessTime());
        result.setLastUpdateTime(localMapStats.getLastUpdateTime());
        result.setHits(localMapStats.getHits());
        result.setLockedEntryCount(localMapStats.getLockedEntryCount());
        result.setDirtyEntryCount(localMapStats.getDirtyEntryCount());
        result.setPutOperationCount(localMapStats.getPutOperationCount());
        result.setGetOperationCount(localMapStats.getGetOperationCount());
        result.setRemoveOperationCount(localMapStats.getRemoveOperationCount());
        result.setTotalPutLatency(localMapStats.getTotalPutLatency());
        result.setTotalGetLatency(localMapStats.getTotalGetLatency());
        result.setTotalRemoveLatency(localMapStats.getTotalRemoveLatency());
        result.setMaxPutLatency(localMapStats.getMaxPutLatency());
        result.setMaxGetLatency(localMapStats.getMaxGetLatency());
        result.setMaxRemoveLatency(localMapStats.getMaxRemoveLatency());
        result.setEventOperationCount(localMapStats.getEventOperationCount());
        result.setOtherOperationCount(localMapStats.getOtherOperationCount());
        result.setTotal(localMapStats.total());
        result.setHeapCost(localMapStats.getHeapCost());
        try {
            result.setNearCacheStatistics(
                    NearCacheStats.fromHazelcast(
                            localMapStats.getNearCacheStats()
                    )
            );
        } catch (UnsupportedOperationException e) {
            // Just ignore the exception
            // Caches (server-side) and replicated maps do not support near caches
        }

        return result;
    }

    public long getOwnedEntryCount() {
        return ownedEntryCount;
    }

    public MapStats setOwnedEntryCount(long ownedEntryCount) {
        this.ownedEntryCount = ownedEntryCount;
        return this;
    }

    public long getBackupEntryCount() {
        return backupEntryCount;
    }

    public MapStats setBackupEntryCount(long backupEntryCount) {
        this.backupEntryCount = backupEntryCount;
        return this;
    }

    public int getBackupCount() {
        return backupCount;
    }

    public MapStats setBackupCount(int backupCount) {
        this.backupCount = backupCount;
        return this;
    }

    public long getOwnedEntryMemoryCost() {
        return ownedEntryMemoryCost;
    }

    public MapStats setOwnedEntryMemoryCost(long ownedEntryMemoryCost) {
        this.ownedEntryMemoryCost = ownedEntryMemoryCost;
        return this;
    }

    public long getBackupEntryMemoryCost() {
        return backupEntryMemoryCost;
    }

    public MapStats setBackupEntryMemoryCost(long backupEntryMemoryCost) {
        this.backupEntryMemoryCost = backupEntryMemoryCost;
        return this;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public MapStats setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public MapStats setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
        return this;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public MapStats setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    public long getHits() {
        return hits;
    }

    public MapStats setHits(long hits) {
        this.hits = hits;
        return this;
    }

    public long getLockedEntryCount() {
        return lockedEntryCount;
    }

    public MapStats setLockedEntryCount(long lockedEntryCount) {
        this.lockedEntryCount = lockedEntryCount;
        return this;
    }

    public long getDirtyEntryCount() {
        return dirtyEntryCount;
    }

    public MapStats setDirtyEntryCount(long dirtyEntryCount) {
        this.dirtyEntryCount = dirtyEntryCount;
        return this;
    }

    public long getPutOperationCount() {
        return putOperationCount;
    }

    public MapStats setPutOperationCount(long putOperationCount) {
        this.putOperationCount = putOperationCount;
        return this;
    }

    public long getGetOperationCount() {
        return getOperationCount;
    }

    public MapStats setGetOperationCount(long getOperationCount) {
        this.getOperationCount = getOperationCount;
        return this;
    }

    public long getRemoveOperationCount() {
        return removeOperationCount;
    }

    public MapStats setRemoveOperationCount(long removeOperationCount) {
        this.removeOperationCount = removeOperationCount;
        return this;
    }

    public long getTotalPutLatency() {
        return totalPutLatency;
    }

    public MapStats setTotalPutLatency(long totalPutLatency) {
        this.totalPutLatency = totalPutLatency;
        return this;
    }

    public long getTotalGetLatency() {
        return totalGetLatency;
    }

    public MapStats setTotalGetLatency(long totalGetLatency) {
        this.totalGetLatency = totalGetLatency;
        return this;
    }

    public long getTotalRemoveLatency() {
        return totalRemoveLatency;
    }

    public MapStats setTotalRemoveLatency(long totalRemoveLatency) {
        this.totalRemoveLatency = totalRemoveLatency;
        return this;
    }

    public long getMaxPutLatency() {
        return maxPutLatency;
    }

    public MapStats setMaxPutLatency(long maxPutLatency) {
        this.maxPutLatency = maxPutLatency;
        return this;
    }

    public long getMaxGetLatency() {
        return maxGetLatency;
    }

    public MapStats setMaxGetLatency(long maxGetLatency) {
        this.maxGetLatency = maxGetLatency;
        return this;
    }

    public long getMaxRemoveLatency() {
        return maxRemoveLatency;
    }

    public MapStats setMaxRemoveLatency(long maxRemoveLatency) {
        this.maxRemoveLatency = maxRemoveLatency;
        return this;
    }

    public long getEventOperationCount() {
        return eventOperationCount;
    }

    public MapStats setEventOperationCount(long eventOperationCount) {
        this.eventOperationCount = eventOperationCount;
        return this;
    }

    public long getOtherOperationCount() {
        return otherOperationCount;
    }

    public MapStats setOtherOperationCount(long otherOperationCount) {
        this.otherOperationCount = otherOperationCount;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public MapStats setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getHeapCost() {
        return heapCost;
    }

    public MapStats setHeapCost(long heapCost) {
        this.heapCost = heapCost;
        return this;
    }

    public NearCacheStats getNearCacheStatistics() {
        return nearCacheStatistics;
    }

    public MapStats setNearCacheStatistics(NearCacheStats nearCacheStatistics) {
        this.nearCacheStatistics = nearCacheStatistics;
        return this;
    }
}
