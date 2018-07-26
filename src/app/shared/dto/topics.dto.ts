export interface AbstractTopicDTO {
  topicType: 'clusters' | 'stats' | 'members' | 'maps' | 'map';
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

export interface MapsTopicDTO extends AbstractTopicDTO {
  topicType: 'maps';
}

export interface MapTopicDTO extends AbstractTopicDTO {
  topicType: 'map';
  mapName: string;
}
