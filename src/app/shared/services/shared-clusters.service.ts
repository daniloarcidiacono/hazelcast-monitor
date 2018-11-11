import {Injectable} from '@angular/core';
import {Cluster} from '../model/shared-cluster.model';

@Injectable()
export class SharedClustersService {
  private clusters: Cluster[];
  private currentCluster: Cluster;

  public constructor() {
    this.clusters = [];
    this.currentCluster = undefined;
  }

  public setClusters(clusters: Cluster[]): void {
    this.clusters = clusters;
    // this.clusters.push(...clusters);
  }

  public getCurrentCluster(): Cluster {
    return this.currentCluster;
  }

  public hasClusters(): boolean {
    return this.clusters.length > 0;
  }

  public setCurrentCluster(cluster: Cluster): void {
    this.currentCluster = cluster;
  }

  public hasCurrentCluster(): boolean {
    return this.currentCluster !== undefined;
  }

  public getClusters(): Cluster[] {
    return this.clusters;
  }
}
