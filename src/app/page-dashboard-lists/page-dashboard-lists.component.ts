import {Component, OnDestroy} from '@angular/core';
import {ListsProductDTO, ListSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {PageDashboardListComponent} from '../page-dashboard-list/page-dashboard-list.component';
import {SharedPageIconsConstants} from "@shared/constants/shared-page-icons.constants";

@Component({
  templateUrl: './page-dashboard-lists.component.html',
  styleUrls: [ './page-dashboard-lists.component.scss' ]
})
export class PageDashboardListsComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: ListsProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public navigateToListDetails(row: ListSummaryDTO): void {
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

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToLists(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<ListsProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the lists: ${error.errors}`);
        }
      );
    }
  }

  public beforeHide(): void {
    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
      this.dataSub = undefined;
    }
  }

  public get clusterName(): string {
    return this.clustersService.getCurrentCluster().instanceName;
  }

  public tabCreated(tab: TabData): void {
  }
}
