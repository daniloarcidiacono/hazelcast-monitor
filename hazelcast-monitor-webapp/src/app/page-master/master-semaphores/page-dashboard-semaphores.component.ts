import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {SemaphoresProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';

@Component({
  templateUrl: './page-dashboard-semaphores.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardSemaphoresComponent extends SharedMasterComponent<SemaphoresProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService) {
    super(DistributedObjectType.SEMAPHORE, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Semaphores of ${this.clusterName}`;
  }
}
