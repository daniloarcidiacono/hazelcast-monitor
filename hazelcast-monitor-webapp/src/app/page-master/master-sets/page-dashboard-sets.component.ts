import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {SetsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {SetSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardSetComponent} from '../../page-dashboard-set/page-dashboard-set.component';

@Component({
  templateUrl: './page-dashboard-sets.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardSetsComponent extends SharedMasterComponent<SetsProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    super(DistributedObjectType.SET, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Sets of ${this.clusterName}`;
  }

  public navigateToDetails(row: SetSummaryDTO): void {
    const setName: string = row.name;

    this.tabsService.addTab({
      label: setName,
      icon: SharedPageIconsConstants.SETS_ICON,
      componentClass: PageDashboardSetComponent,
      componentInputs: {
        setName: setName
      }
    });
  }
}
