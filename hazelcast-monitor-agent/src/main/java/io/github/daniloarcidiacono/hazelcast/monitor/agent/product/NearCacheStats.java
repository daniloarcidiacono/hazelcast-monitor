package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

import java.io.Serializable;
import java.util.Collection;

@TypescriptDTO
public class NearCacheStats implements Serializable {
    @TypescriptComments("Creation time of this Near Cache on this member")
    private long creationTime = 0;

    @TypescriptComments("Number of Near Cache entries owned by this member")
    private long ownedEntryCount = 0;

    @TypescriptComments("Memory cost (number of bytes) of Near Cache entries owned by this member")
    private long ownedEntryMemoryCost = 0;

    @TypescriptComments("Number of hits (reads) of Near Cache entries owned by this member")
    private long hits = 0;

    @TypescriptComments("Number of misses of Near Cache entries owned by this member")
    private long misses = 0;

    @TypescriptComments("Hit/miss ratio of Near Cache entries owned by this member")
    private double ratio = 0;

    @TypescriptComments("Number of evictions of Near Cache entries owned by this member")
    private long evictions = 0;

    @TypescriptComments("Number of TTL and max-idle expirations of Near Cache entries owned by this member")
    private long expirations = 0;

    @TypescriptComments("Number of Near Cache key persistences (when the pre-load feature is enabled)")
    private long persistenceCount = 0;

    @TypescriptComments("Timestamp of the last Near Cache key persistence (when the pre-load feature is enabled)")
    private long lastPersistenceTime = Long.MIN_VALUE;

    @TypescriptComments("Duration in milliseconds of the last Near Cache key persistence (when the pre-load feature is enabled)")
    private long lastPersistenceDuration = Long.MIN_VALUE;

    @TypescriptComments("Written bytes of the last Near Cache key persistence (when the pre-load feature is enabled)")
    private long lastPersistenceWrittenBytes = Long.MIN_VALUE;

    @TypescriptComments("Number of persisted keys of the last Near Cache key persistence (when the pre-load feature is enabled)")
    private long lastPersistenceKeyCount = Long.MIN_VALUE;

    @TypescriptComments("Failure reason of the last Near Cache persistence (when the pre-load feature is enabled)")
    private String lastPersistenceFailure;

    public NearCacheStats() {
    }

    public static NearCacheStats aggregated(final Collection<NearCacheStats> stats) {
        final int statCount = stats.size();
        final NearCacheStats result = new NearCacheStats();
        for (NearCacheStats stat : stats) {
            if (stat == null) {
                continue;
            }

            result.creationTime += stat.creationTime / statCount;
            result.ownedEntryCount += stat.ownedEntryCount;
            result.ownedEntryMemoryCost += stat.ownedEntryMemoryCost;
            result.hits += stat.hits;
            result.misses += stat.misses;
            result.evictions += stat.evictions;
            result.expirations += stat.expirations;
            result.persistenceCount += stat.persistenceCount;
            result.lastPersistenceTime = Math.max(result.lastPersistenceTime, stat.lastPersistenceTime);
            result.lastPersistenceDuration = Math.max(result.lastPersistenceDuration, stat.lastPersistenceDuration);
            result.lastPersistenceWrittenBytes = Math.max(result.lastPersistenceWrittenBytes, stat.lastPersistenceWrittenBytes);
            result.lastPersistenceKeyCount = Math.max(result.lastPersistenceKeyCount, stat.lastPersistenceKeyCount);
        }

        if (result.misses == 0) {
            result.ratio = result.hits == 0 ? Double.NaN : Double.POSITIVE_INFINITY;
        } else {
            result.ratio = result.hits / (float)result.misses * 100.0f;
        }

        // This property cannot be aggregated
        result.lastPersistenceFailure = null;
        return result;
    }

    public static NearCacheStats fromHazelcast(final com.hazelcast.monitor.NearCacheStats nearCacheStats) {
        if (nearCacheStats == null) {
            return null;
        }

        final NearCacheStats result = new NearCacheStats();
        result.setCreationTime(nearCacheStats.getCreationTime());
        result.setOwnedEntryCount(nearCacheStats.getOwnedEntryCount());
        result.setOwnedEntryMemoryCost(nearCacheStats.getOwnedEntryMemoryCost());
        result.setHits(nearCacheStats.getHits());
        result.setMisses(nearCacheStats.getMisses());
        result.setRatio(nearCacheStats.getRatio());
        result.setEvictions(nearCacheStats.getEvictions());
        result.setExpirations(nearCacheStats.getExpirations());
        result.setPersistenceCount(nearCacheStats.getPersistenceCount());
        result.setLastPersistenceTime(nearCacheStats.getLastPersistenceTime());
        result.setLastPersistenceDuration(nearCacheStats.getLastPersistenceDuration());
        result.setLastPersistenceWrittenBytes(nearCacheStats.getLastPersistenceWrittenBytes());
        result.setLastPersistenceKeyCount(nearCacheStats.getLastPersistenceKeyCount());
        result.setLastPersistenceFailure(nearCacheStats.getLastPersistenceFailure());
        if (result.getLastPersistenceFailure().isEmpty()) {
            result.setLastPersistenceFailure(null);
        }

        return result;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public NearCacheStats setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public long getOwnedEntryCount() {
        return ownedEntryCount;
    }

    public NearCacheStats setOwnedEntryCount(long ownedEntryCount) {
        this.ownedEntryCount = ownedEntryCount;
        return this;
    }

    public long getOwnedEntryMemoryCost() {
        return ownedEntryMemoryCost;
    }

    public NearCacheStats setOwnedEntryMemoryCost(long ownedEntryMemoryCost) {
        this.ownedEntryMemoryCost = ownedEntryMemoryCost;
        return this;
    }

    public long getHits() {
        return hits;
    }

    public NearCacheStats setHits(long hits) {
        this.hits = hits;
        return this;
    }

    public long getMisses() {
        return misses;
    }

    public NearCacheStats setMisses(long misses) {
        this.misses = misses;
        return this;
    }

    public double getRatio() {
        return ratio;
    }

    public NearCacheStats setRatio(double ratio) {
        this.ratio = ratio;
        return this;
    }

    public long getEvictions() {
        return evictions;
    }

    public NearCacheStats setEvictions(long evictions) {
        this.evictions = evictions;
        return this;
    }

    public long getExpirations() {
        return expirations;
    }

    public NearCacheStats setExpirations(long expirations) {
        this.expirations = expirations;
        return this;
    }

    public long getPersistenceCount() {
        return persistenceCount;
    }

    public NearCacheStats setPersistenceCount(long persistenceCount) {
        this.persistenceCount = persistenceCount;
        return this;
    }

    public long getLastPersistenceTime() {
        return lastPersistenceTime;
    }

    public NearCacheStats setLastPersistenceTime(long lastPersistenceTime) {
        this.lastPersistenceTime = lastPersistenceTime;
        return this;
    }

    public long getLastPersistenceDuration() {
        return lastPersistenceDuration;
    }

    public NearCacheStats setLastPersistenceDuration(long lastPersistenceDuration) {
        this.lastPersistenceDuration = lastPersistenceDuration;
        return this;
    }

    public long getLastPersistenceWrittenBytes() {
        return lastPersistenceWrittenBytes;
    }

    public NearCacheStats setLastPersistenceWrittenBytes(long lastPersistenceWrittenBytes) {
        this.lastPersistenceWrittenBytes = lastPersistenceWrittenBytes;
        return this;
    }

    public long getLastPersistenceKeyCount() {
        return lastPersistenceKeyCount;
    }

    public NearCacheStats setLastPersistenceKeyCount(long lastPersistenceKeyCount) {
        this.lastPersistenceKeyCount = lastPersistenceKeyCount;
        return this;
    }

    public String getLastPersistenceFailure() {
        return lastPersistenceFailure;
    }

    public NearCacheStats setLastPersistenceFailure(String lastPersistenceFailure) {
        this.lastPersistenceFailure = lastPersistenceFailure;
        return this;
    }
}
