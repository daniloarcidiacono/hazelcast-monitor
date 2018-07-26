export interface ProductDTO {
}

export interface ClustersProductDTO extends ProductDTO{
  clusters: string[];
}

export interface StatisticsProductDTO extends ProductDTO {
  membersCount: number;
  mapCount: number;
  queueCount: number;
  lockCount: number;
  topicCount: number;
  listCount: number;
  setCount: number;
  multiMapCount: number;
  ringbufferCount: number;
}

export interface MembersProductDTO extends ProductDTO {
  members: MemberSummaryDTO[];
}

export interface MemberSummaryDTO {
  address: string;
  port: number;
  version: string;
  uuid: string;
}

export interface MapsProductDTO extends ProductDTO {
  maps: MapSummaryDTO[];
}

export interface MapSummaryDTO {
  name: string;
  size: number;
}

export interface MapProductDTO extends ProductDTO {
  data: EntryDTO[];
}

export interface EntryDTO {
  key: any;
  value: any;
  keyString: string;
  valueString: string;
}
