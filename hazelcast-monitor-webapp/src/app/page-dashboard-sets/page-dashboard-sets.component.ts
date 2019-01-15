import {Component, OnDestroy} from '@angular/core';
import {SetsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SetSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {PageDashboardSetComponent} from "../page-dashboard-set/page-dashboard-set.component";
import {SharedPageIconsConstants} from "@shared/constants/shared-page-icons.constants";

@Component({
  templateUrl: './page-dashboard-sets.component.html',
  styleUrls: [ './page-dashboard-sets.component.scss' ]
})
export class PageDashboardSetsComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: SetsProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public navigateToListDetails(row: SetSummaryDTO): void {
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

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToSets(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<SetsProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the sets: ${error.errors}`);
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
