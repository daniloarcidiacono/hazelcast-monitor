export interface ProductDTO {
}

export interface ClustersProductDTO extends ProductDTO {
  clusters: string[];
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
