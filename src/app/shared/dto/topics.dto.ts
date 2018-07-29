export interface AbstractTopicDTO {
  topicType: 'clusters' | 'stats' | 'members' | 'distributed_object' | 'distributed_object_details';
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

export interface DistributedObjectTopicDTO extends AbstractTopicDTO {
  topicType: 'distributed_object_details';
  distributedObjectType: DistributedObjectType;
  objectName: string;
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

