import {EnvironmentVariableDTO, SubscriptionRegistryStatisticsDTO} from "@shared/dto/internals.dto";

export interface ProductDTO {
  produceTime?: number;
}

export interface ClustersProductDTO extends ProductDTO {
  clusters: string[];
}

export interface InternalsProductDTO extends ProductDTO {
  subscriptionStats: SubscriptionRegistryStatisticsDTO;
  envVariables: EnvironmentVariableDTO[];
  memberConfig: string;
}

export interface StatisticsProductDTO extends ProductDTO {
  membersCount: number;
  atomicLongCount: number;
  atomicReferenceCount: number;
  cacheCount: number;
  countDownLatchCount: number;
  listCount: number;
  lockCount: number;
  mapCount: number;
  multiMapCount: number;
  queueCount: number;
  replicatedMapCount: number;
  ringbufferCount: number;
  semaphoreCount: number;
  setCount: number;
  topicCount: number;
  systemCpuLoad: number;
  processCpuLoad: number;
  totalPhysicalMemory: number;
  freePhysicalMemory: number;
  maxHeapMemory: number;
  usedHeapMemory: number;
  maxNativeMemory: number;
  freeNativeMemory: number;
  cardinalityEstimatorsCount: number;
  executorsCount: number;
}


export interface MembersProductDTO extends ProductDTO {
  objects: MemberSummaryDTO[];
}

export interface MemberSummaryDTO {
  address: string;
  port: number;
  version: string;
  uuid: string;
}

export interface DistributedObjectsProduct<T extends DistributedObjectSummary> extends ProductDTO {
  objects: T[];
}

export interface DistributedObjectSummary {
  name: string;
  partitionKey: string;
}

// Locks
export interface LockSummaryDTO extends DistributedObjectSummary {
  lockCount: number;
  remainingLeaseTime: number;
  locked: boolean;
}
export type LocksProductDTO = DistributedObjectsProduct<LockSummaryDTO>;

// Maps
export interface MapSummaryDTO extends DistributedObjectSummary {
  size: number;
}
export type MapsProductDTO = DistributedObjectsProduct<MapSummaryDTO>;

// Lists
export interface ListSummaryDTO extends DistributedObjectSummary {
  size: number;
}
export type ListsProductDTO = DistributedObjectsProduct<ListSummaryDTO>;

// Multi Maps
export interface MultiMapSummaryDTO extends DistributedObjectSummary {
  keyCount: number;
  valueCount: number;
}
export type MultiMapsProductDTO = DistributedObjectsProduct<MultiMapSummaryDTO>;

// Queues
export interface QueueSummaryDTO extends DistributedObjectSummary {
  size: number;
}
export type QueuesProductDTO = DistributedObjectsProduct<QueueSummaryDTO>;

// Sets
export interface SetSummaryDTO extends DistributedObjectSummary {
  size: number;
}
export type SetsProductDTO = DistributedObjectsProduct<SetSummaryDTO>;

// Replicated maps
export interface ReplicatedMapSummaryDTO extends DistributedObjectSummary {
  size: number;
}
export type ReplicatedMapsProductDTO = DistributedObjectsProduct<ReplicatedMapSummaryDTO>;

// Topics
export interface TopicSummaryDTO extends DistributedObjectSummary {
}
export type TopicsProductDTO = DistributedObjectsProduct<TopicSummaryDTO>;

// Atomic longs
export interface AtomicLongSummaryDTO extends DistributedObjectSummary {
  value: number;
}
export type AtomicLongsProductDTO = DistributedObjectsProduct<AtomicLongSummaryDTO>;

// Atomic references
export interface AtomicReferenceSummaryDTO extends DistributedObjectSummary {
  value: any;
  valueString: string;
}
export type AtomicReferencesProductDTO = DistributedObjectsProduct<AtomicReferenceSummaryDTO>;

// Caches
export interface CacheSummaryDTO extends DistributedObjectSummary {
  size: number;
  destroyed: boolean;
}
export type CachesProductDTO = DistributedObjectsProduct<CacheSummaryDTO>;

// Executor
export interface ExecutorSummaryDTO extends DistributedObjectSummary {
}
export type ExecutorsProductDTO = DistributedObjectsProduct<ExecutorSummaryDTO>;

// Cardinality estimator
export interface CardinalityEstimatorSummaryDTO extends DistributedObjectSummary {
  estimate: number;
}
export type CardinalityEstimatorsProductDTO = DistributedObjectsProduct<CardinalityEstimatorSummaryDTO>;

// Count down latches
export interface CountDownLatchSummaryDTO extends DistributedObjectSummary {
  count: number;
}
export type CountDownLatchesProductDTO = DistributedObjectsProduct<CountDownLatchSummaryDTO>;

// Ring buffers
export interface RingbufferSummaryDTO extends DistributedObjectSummary {
  size: number;
  capacity: number;
  remainingCapacity: number;
  headSequence: number;
  tailSequence: number;
}
export type RingbuffersProductDTO = DistributedObjectsProduct<RingbufferSummaryDTO>;

// Semaphores
export interface SemaphoreSummaryDTO extends DistributedObjectSummary {
  permits: number;
}
export type SemaphoresProductDTO = DistributedObjectsProduct<SemaphoreSummaryDTO>;

// Topic
export interface TopicProductDTO extends ProductDTO {
  publisher: MemberSummaryDTO;
  message: any;
  messageString: string;
  publishTime: number;
}

// Map
export interface MapProductDTO extends ProductDTO {
  entries: MapProductEntryDTO[];
}

export interface MapProductEntryDTO {
  key: any;
  value: any;
  keyString: string;
  valueString: string;
  locked: boolean;
}

// Multi Map
export type MultiMapProductDTO = MapProductDTO;
export type MultiMapProductEntryDTO = MapProductEntryDTO;

// Cache
export type CacheProductDTO = MapProductDTO;
export type CacheProductEntryDTO = MapProductEntryDTO;

// List
export interface ListProductDTO extends ProductDTO {
  entries: ListProductEntryDTO[];
}

export interface ListProductEntryDTO {
  value: any;
  valueString: string;
}

// Queue
export type QueueProductDTO = ListProductDTO;
export type QueueProductEntryDTO = ListProductEntryDTO;

// Set
export type SetProductDTO = ListProductDTO;
export type SetProductEntryDTO = ListProductEntryDTO;

// Replicated Map
export type ReplicatedMapProductDTO = MapProductDTO;
export type ReplicatedMapProductEntryDTO = MapProductEntryDTO;

// Stats
export interface StatsProductDTO<T> extends ProductDTO {
  sampleTime: number;
  members: { [ index: string ]: T };
  aggregated: T;
}

// Topic Stats
export interface TopicStatsDTO {
  // Total number of received messages of this topic on this member.
  receiveOperationCount: number;

  // Total number of published messages of this topic on this member.
  publishOperationCount: number;

  // Creation time of this topic on this member.
  creationTime: number;
}
export type TopicStatsProductDTO = StatsProductDTO<TopicStatsDTO>;

// Queue stats
export interface QueueStatsDTO {
  // Creation time on this member.
  creationTime: number;

  // Number of owned items in this member.
  ownedItemCount: number;

  // Number of backup items in this member.
  backupItemCount: number;

  // Minimum age of the items in this member.
  minAge?: number;

  // Maximum age of the items in this member.
  maxAge?: number;

  // Average age of the items in this member.
  avgAge: number;

  /**
   * Number of offer/put/add operations.
   * Offers returning false will be included.
   */
  offerOperationCount: number;

  /**
   * Number of rejected offers.
   * Offer can be rejected because of max-size limit on the queue.
   */
  rejectedOfferOperationCount: number;

  /**
   * Number of poll/take/remove operations.
   * Polls returning null (empty) will be included.
   */
  pollOperationCount: number;

  /**
   * Number of null returning poll operations.
   * Poll operation might return null, if the queue is empty.
   */
  emptyPollOperationCount: number;

  // Number of other operations
  otherOperationsCount: number;

  // Number of event operations
  eventOperationCount: number;
}
export type QueueStatsProductDTO = StatsProductDTO<QueueStatsDTO>;
