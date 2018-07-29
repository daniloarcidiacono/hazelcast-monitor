export interface AbstractTopicDTO {
  topicType: 'clusters' | 'stats' | 'members' | 'distributed_object' | 'map';
  instanceName: string;
}

export interface ClustersTopicDTO extends AbstractTopicDTO {
  topicType: 'clusters';
}

export interface StatisticsTopicDTO extends AbstractTopicDTO {
  topicType: 'stats';
}

export interface MembersTopicDTO extends AbstractTopicDTO {
  topicType: 'members';
}

export interface DistributedObjectsTopicDTO extends AbstractTopicDTO {
  topicType: 'distributed_object';
  distributedObjectType: DistributedObjectType;
}

export interface MapTopicDTO extends AbstractTopicDTO {
  topicType: 'map';
  mapName: string;
}

export enum DistributedObjectType {
  ATOMICLONG = 'ATOMICLONG',
  ATOMICREFERENCE = 'ATOMICREFERENCE',
  CACHE = 'CACHE',
  COUNTDOWNLATCH = 'COUNTDOWNLATCH',
  LIST = 'LIST',
  LOCK = 'LOCK',
  MAP = 'MAP',
  MULTIMAP = 'MULTIMAP',
  QUEUE = 'QUEUE',
  REPLICATEDMAP = 'REPLICATEDMAP',
  RINGBUFFER = 'RINGBUFFER',
  SEMAPHORE = 'SEMAPHORE',
  SET = 'SET',
  TOPIC = 'TOPIC'
}

