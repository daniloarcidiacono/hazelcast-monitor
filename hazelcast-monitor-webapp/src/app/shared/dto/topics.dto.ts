/**
 * Hazelcast Monitor 0.0.1 DTOs
 * -------------------------------------
 * This file is automatically generated by TypescriptMapper.
 * Do not modify this file -- YOUR CHANGES WILL BE ERASED!
 */
export interface AbstractTopicDTO {
	topicType: 'stats' | 'clusters' | 'internals' | 'members' | 'distributed_object' | 'distributed_object_details' | 'distributed_object_stats';
	instanceName: string;
}

export interface ClustersTopicDTO extends AbstractTopicDTO {
	topicType: 'clusters';
}

export interface DistributedObjectStatsTopicDTO extends AbstractTopicDTO {
	topicType: 'distributed_object_stats';
	distributedObjectType: DistributedObjectType;
	objectName: string;
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
	CARDINALITYESTIMATOR = 'CARDINALITYESTIMATOR',
	COUNTDOWNLATCH = 'COUNTDOWNLATCH',
	EXECUTOR = 'EXECUTOR',
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

export interface DistributedObjectsTopicDTO extends AbstractTopicDTO {
	topicType: 'distributed_object';
	distributedObjectType: DistributedObjectType;
}

export interface InternalsTopicDTO extends AbstractTopicDTO {
	topicType: 'internals';
}

export interface MembersTopicDTO extends AbstractTopicDTO {
	topicType: 'members';
}

export interface StatisticsTopicDTO extends AbstractTopicDTO {
	topicType: 'stats';
}
