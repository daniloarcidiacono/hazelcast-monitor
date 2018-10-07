import {Component, OnDestroy} from '@angular/core';
import {CachesProductDTO, CacheSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {PageDashboardCacheComponent} from '../page-dashboard-cache/page-dashboard-cache.component';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedPageIconsConstants} from "@shared/constants/shared-page-icons.constants";

@Component({
  templateUrl: './page-dashboard-caches.component.html',
  styleUrls: [ './page-dashboard-caches.component.scss' ]
})
export class PageDashboardCachesComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: CachesProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public navigateToCacheDetails(row: CacheSummaryDTO): void {
    const cacheName: string = row.name;

    this.tabsService.addTab({
      label: cacheName,
      icon: SharedPageIconsConstants.CACHES_ICON,
      componentClass: PageDashboardCacheComponent,
      componentInputs: {
        cacheName: cacheName
      }
    });
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToCaches(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<CachesProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the caches: ${error.errors}`);
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
