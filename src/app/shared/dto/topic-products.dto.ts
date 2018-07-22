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

export interface MapProductDTO extends ProductDTO {
  data: EntryDTO[];
}

export interface EntryDTO {
  key: any;
  value: any;
}

export interface MembersProductDTO extends ProductDTO {
  members: MemberDTO[];
}

export interface MemberDTO {
  address: string;
  port: number;
  version: string;
  uuid: string;
}
