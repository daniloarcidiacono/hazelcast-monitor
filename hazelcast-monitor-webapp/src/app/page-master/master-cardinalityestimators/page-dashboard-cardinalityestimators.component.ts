import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {CardinalityEstimatorsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';

@Component({
  templateUrl: './page-dashboard-cardinalityestimators.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardCardinalityEstimatorsComponent extends SharedMasterComponent<CardinalityEstimatorsProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService) {
    super(DistributedObjectType.CARDINALITYESTIMATOR, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Cardinality estimators of ${this.clusterName}`;
  }
}
