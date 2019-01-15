import {
  AtomicLongSummaryDTO,
  AtomicReferenceSummaryDTO,
  CacheStatsDTO,
  CacheSummaryDTO,
  CardinalityEstimatorSummaryDTO,
  CountDownLatchSummaryDTO,
  DistributedObjectsProductDTO,
  ExecutorStatsDTO,
  ExecutorSummaryDTO,
  ListProductDTO,
  ListProductEntryDTO,
  ListSummaryDTO,
  LockSummaryDTO,
  MapProductDTO,
  MapProductEntryDTO,
  MapStatsDTO,
  MapSummaryDTO,
  MultiMapSummaryDTO,
  QueueStatsDTO,
  QueueSummaryDTO,
  ReplicatedMapSummaryDTO,
  RingbufferSummaryDTO,
  SemaphoreSummaryDTO,
  SetSummaryDTO,
  StatsProductDTO,
  TopicStatsDTO,
  TopicSummaryDTO
} from "@shared/dto/topic-products.dto";

// Locks
export type LocksProductDTO = DistributedObjectsProductDTO<LockSummaryDTO>;

// Maps
export type MapsProductDTO = DistributedObjectsProductDTO<MapSummaryDTO>;

// Lists
export type ListsProductDTO = DistributedObjectsProductDTO<ListSummaryDTO>;

// Multi Maps
export type MultiMapsProductDTO = DistributedObjectsProductDTO<MultiMapSummaryDTO>;

// Queues
export type QueuesProductDTO = DistributedObjectsProductDTO<QueueSummaryDTO>;

// Sets
export type SetsProductDTO = DistributedObjectsProductDTO<SetSummaryDTO>;

// Replicated maps
export type ReplicatedMapsProductDTO = DistributedObjectsProductDTO<ReplicatedMapSummaryDTO>;

// Topics
export type TopicsProductDTO = DistributedObjectsProductDTO<TopicSummaryDTO>;

// Atomic longs
export type AtomicLongsProductDTO = DistributedObjectsProductDTO<AtomicLongSummaryDTO>;

// Atomic references
export type AtomicReferencesProductDTO = DistributedObjectsProductDTO<AtomicReferenceSummaryDTO>;

// Caches
export type CachesProductDTO = DistributedObjectsProductDTO<CacheSummaryDTO>;

// Executor
export type ExecutorsProductDTO = DistributedObjectsProductDTO<ExecutorSummaryDTO>;

// Cardinality estimator
export type CardinalityEstimatorsProductDTO = DistributedObjectsProductDTO<CardinalityEstimatorSummaryDTO>;

// Count down latches
export type CountDownLatchesProductDTO = DistributedObjectsProductDTO<CountDownLatchSummaryDTO>;

// Ring buffers
export type RingbuffersProductDTO = DistributedObjectsProductDTO<RingbufferSummaryDTO>;

// Semaphores
export type SemaphoresProductDTO = DistributedObjectsProductDTO<SemaphoreSummaryDTO>;

// Multi Map
export type MultiMapProductDTO = MapProductDTO;
export type MultiMapProductEntryDTO = MapProductEntryDTO;

// Cache
export type CacheProductDTO = MapProductDTO;
export type CacheProductEntryDTO = MapProductEntryDTO;

// Queue
export type QueueProductDTO = ListProductDTO;
export type QueueProductEntryDTO = ListProductEntryDTO;

// Set
export type SetProductDTO = ListProductDTO;
export type SetProductEntryDTO = ListProductEntryDTO;

// Replicated Map
export type ReplicatedMapProductDTO = MapProductDTO;
export type ReplicatedMapProductEntryDTO = MapProductEntryDTO;

// Topic Stats
export type TopicStatsProductDTO = StatsProductDTO<TopicStatsDTO>;

// Queue stats
export type QueueStatsProductDTO = StatsProductDTO<QueueStatsDTO>;

// Executor stats
export type ExecutorStatsProductDTO = StatsProductDTO<ExecutorStatsDTO>;

// Cache stats
export type CacheStatsProductDTO = StatsProductDTO<CacheStatsDTO>;

// Near cache stats
export type MapStatsProductDTO = StatsProductDTO<MapStatsDTO>;
