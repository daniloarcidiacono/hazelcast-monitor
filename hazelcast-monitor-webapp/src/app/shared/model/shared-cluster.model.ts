/**
 * Hazelcast group object.
 */
export class Cluster {
  public instanceName: string;
  public groupName: string;

  public constructor(instanceName: string, groupName: string) {
    this.instanceName = instanceName;
    this.groupName = groupName;
  }
}
