import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {ListsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {ListSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardListComponent} from '../../page-dashboard-list/page-dashboard-list.component';

@Component({
  templateUrl: './page-dashboard-lists.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardListsComponent extends SharedMasterComponent<ListsProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    super(DistributedObjectType.LIST, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Lists of ${this.clusterName}`;
  }

  public navigateToDetails(row: ListSummaryDTO): void {
    const listName: string = row.name;

    this.tabsService.addTab({
      label: listName,
      icon: SharedPageIconsConstants.LISTS_ICON,
      componentClass: PageDashboardListComponent,
      componentInputs: {
        listName: listName
      }
    });
  }
}
