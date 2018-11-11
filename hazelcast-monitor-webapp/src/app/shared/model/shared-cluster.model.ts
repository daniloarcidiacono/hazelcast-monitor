/**
 * Hazelcast group object.
 */
export class Cluster {
  public instanceName: string;

  public constructor(instanceName: string) {
    this.instanceName = instanceName;
  }
}
