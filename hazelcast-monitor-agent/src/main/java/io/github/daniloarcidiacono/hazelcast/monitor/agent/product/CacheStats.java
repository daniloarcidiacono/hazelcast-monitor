package io.github.daniloarcidiacono.hazelcast.monitor.agent.product;

import com.hazelcast.cache.CacheStatistics;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.io.Serializable;
import java.util.Collection;

@TypescriptDTO
public class CacheStats implements Serializable {
    @TypescriptComments("Cache creation time")
    private long creationTime = 0;

    @TypescriptComments("Last access time to cache")
    private long lastAccessTime = Long.MIN_VALUE;

    @TypescriptComments("Last update time to cache")
    private long lastUpdateTime = Long.MIN_VALUE;

    @TypescriptComments("Owned entry count in the cache")
    private long ownedEntryCount = 0;

    @TypescriptComments("Number of get requests that were satisfied by the cache")
    private long cacheHits = 0;

    @TypescriptComments("A measure of cache efficiency")
    private float cacheHitPercentage = 0;

    @TypescriptComments("A miss is a get request that is not satisfied")
    private long cacheMisses = 0;

    @TypescriptComments("Percentage of cache accesses that did not find a requested entry in the cache")
    private float cacheMissPercentage = 0;

    @TypescriptComments("Total number of requests to the cache. This will be equal to the sum of the hits and misses")
    private long cacheGets = 0;

    @TypescriptComments("Total number of puts to the cache")
    private long cachePuts = 0;

    @TypescriptComments({
        "Total number of removals from the cache. This does not include evictions,",
        "where the cache itself initiates the removal to make space."
    })
    private long cacheRemovals = 0;

    @TypescriptComments({
        "Total number of evictions from the cache. An eviction is a removal",
        "initiated by the cache itself to free up space. An eviction is not treated as",
        "a removal and does not appear in the removal counts.",
    })
    private long cacheEvictions = 0;

    @TypescriptComments("Mean time in microseconds to execute gets")
    private float averageGetTime = 0;

    @TypescriptComments("Mean time in microseconds to execute puts")
    private float averagePutTime = 0;

    @TypescriptComments("Mean time in microseconds to execute removes")
    private float averageRemoveTime = 0;

    public CacheStats() {
    }

    public static CacheStats aggregated(final Collection<CacheStats> stats) {
        final int statCount = stats.size();
        final CacheStats result = new CacheStats();
        for (CacheStats stat : stats) {
            result.creationTime += stat.creationTime / statCount;
            result.lastAccessTime = Math.max(result.lastAccessTime, stat.lastAccessTime);
            result.lastUpdateTime = Math.max(result.lastUpdateTime, stat.lastUpdateTime);
            result.ownedEntryCount += stat.ownedEntryCount;
            result.cacheHits += stat.cacheHits;
            result.cacheMisses += stat.cacheMisses;
            result.cacheGets += stat.cacheGets;
            result.cachePuts += stat.cachePuts;
            result.cacheRemovals += stat.cacheRemovals;
            result.cacheEvictions += stat.cacheEvictions;
            result.averageGetTime += stat.averageGetTime / statCount;
            result.averagePutTime += stat.averagePutTime / statCount;
            result.averageRemoveTime += stat.averageRemoveTime / statCount;
        }

        result.cacheHitPercentage = result.cacheGets > 0 ? result.cacheHits / (float)result.cacheGets * 100.0f : 0;
        result.cacheMissPercentage = result.cacheGets > 0 ? result.cacheMisses / (float)result.cacheGets * 100.0f : 0;

        return result;
    }

    public static CacheStats fromHazelcast(final CacheStatistics localCacheStats) {
        final CacheStats result = new CacheStats();
        result.setCreationTime(localCacheStats.getCreationTime());
        result.setLastAccessTime(localCacheStats.getLastAccessTime());
        result.setLastUpdateTime(localCacheStats.getLastUpdateTime());
        result.setOwnedEntryCount(localCacheStats.getOwnedEntryCount());
        result.setCacheHits(localCacheStats.getCacheHits());
        result.setCacheHitPercentage(localCacheStats.getCacheHitPercentage());
        result.setCacheMisses(localCacheStats.getCacheMisses());
        result.setCacheMissPercentage(localCacheStats.getCacheMissPercentage());
        result.setCacheGets(localCacheStats.getCacheGets());
        result.setCachePuts(localCacheStats.getCachePuts());
        result.setCacheRemovals(localCacheStats.getCacheRemovals());
        result.setCacheEvictions(localCacheStats.getCacheEvictions());
        result.setAverageGetTime(localCacheStats.getAverageGetTime());
        result.setAveragePutTime(localCacheStats.getAveragePutTime());
        result.setAverageRemoveTime(localCacheStats.getAverageRemoveTime());

        return result;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public CacheStats setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public CacheStats setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
        return this;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public CacheStats setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    public long getOwnedEntryCount() {
        return ownedEntryCount;
    }

    public CacheStats setOwnedEntryCount(long ownedEntryCount) {
        this.ownedEntryCount = ownedEntryCount;
        return this;
    }

    public long getCacheHits() {
        return cacheHits;
    }

    public CacheStats setCacheHits(long cacheHits) {
        this.cacheHits = cacheHits;
        return this;
    }

    public float getCacheHitPercentage() {
        return cacheHitPercentage;
    }

    public CacheStats setCacheHitPercentage(float cacheHitPercentage) {
        this.cacheHitPercentage = cacheHitPercentage;
        return this;
    }

    public long getCacheMisses() {
        return cacheMisses;
    }

    public CacheStats setCacheMisses(long cacheMisses) {
        this.cacheMisses = cacheMisses;
        return this;
    }

    public float getCacheMissPercentage() {
        return cacheMissPercentage;
    }

    public CacheStats setCacheMissPercentage(float cacheMissPercentage) {
        this.cacheMissPercentage = cacheMissPercentage;
        return this;
    }

    public long getCacheGets() {
        return cacheGets;
    }

    public CacheStats setCacheGets(long cacheGets) {
        this.cacheGets = cacheGets;
        return this;
    }

    public long getCachePuts() {
        return cachePuts;
    }

    public CacheStats setCachePuts(long cachePuts) {
        this.cachePuts = cachePuts;
        return this;
    }

    public long getCacheRemovals() {
        return cacheRemovals;
    }

    public CacheStats setCacheRemovals(long cacheRemovals) {
        this.cacheRemovals = cacheRemovals;
        return this;
    }

    public long getCacheEvictions() {
        return cacheEvictions;
    }

    public CacheStats setCacheEvictions(long cacheEvictions) {
        this.cacheEvictions = cacheEvictions;
        return this;
    }

    public float getAverageGetTime() {
        return averageGetTime;
    }

    public CacheStats setAverageGetTime(float averageGetTime) {
        this.averageGetTime = averageGetTime;
        return this;
    }

    public float getAveragePutTime() {
        return averagePutTime;
    }

    public CacheStats setAveragePutTime(float averagePutTime) {
        this.averagePutTime = averagePutTime;
        return this;
    }

    public float getAverageRemoveTime() {
        return averageRemoveTime;
    }

    public CacheStats setAverageRemoveTime(float averageRemoveTime) {
        this.averageRemoveTime = averageRemoveTime;
        return this;
    }
}
