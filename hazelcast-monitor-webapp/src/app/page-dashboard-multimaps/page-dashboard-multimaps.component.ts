import {Component, OnDestroy} from '@angular/core';
import {MapSummaryDTO} from '@shared/dto/topic-products.dto';
import {MultiMapsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {Subscription} from 'rxjs/index';
import {
  ErrorMessageDTO, SubscribeResponseDTO, SubscriptionNoticeResponseDTO, UpdateSubscriptionRequestDTO,
  UpdateSubscriptionResponseDTO
} from '@shared/dto/hazelcast-monitor.dto';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {PageDashboardMultiMapComponent} from '../page-dashboard-multimap/page-dashboard-multimap.component';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardMapStatsComponent} from "../page-dashboard-map-stats/page-dashboard-map-stats.component";
import {DistributedObjectType} from "@shared/dto/topics.dto";

@Component({
  templateUrl: './page-dashboard-multimaps.component.html',
  styleUrls: [ './page-dashboard-multimaps.component.scss' ]
})
export class PageDashboardMultiMapsComponent implements TabAwareComponent, OnDestroy {
  // Data subscription
  private dataSub: Subscription;

  // Current data
  private data: MultiMapsProductDTO = undefined;

  // Update frequency
  public updateFrequency: number = 1;

  // Pagination
  public page: number = 1;
  public pageSize: number = 15;

  // Filtering
  public filterRegex: string = '';

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public trackPageChange(newPage: number): void {
    this.page = newPage;
    this.updateSubscription();
  }

  public trackPageSizeChange(newPageSize: number): void {
    this.pageSize = newPageSize;
    this.updateSubscription();
  }

  public navigateToMultiMapStats(row: MapSummaryDTO): void {
    const mapName: string = row.name;

    this.tabsService.addTab({
      label: `${mapName} statistics`,
      icon: SharedPageIconsConstants.STATS_ICON,
      componentClass: PageDashboardMapStatsComponent,
      componentInputs: {
        mapName: mapName,
        mapType: DistributedObjectType.MULTIMAP
      }
    });
  }

  public navigateToMultiMapDetails(row: MapSummaryDTO): void {
    const mapName: string = row.name;

    this.tabsService.addTab({
      label: mapName,
      icon: SharedPageIconsConstants.MULTIMAPS_ICON,
      componentClass: PageDashboardMultiMapComponent,
      componentInputs: {
        mapName: mapName
      }
    });
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      const parameters: any = {
        frequency: `${this.updateFrequency}`,
        filter: this.filterRegex,
        page: `${this.page}`,
        pageSize: `${this.pageSize}`
      };

      this.dataSub = this.hazelcastService.subscribeToMultiMaps(this.clustersService.getCurrentCluster().instanceName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<MultiMapsProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the multimaps: ${error.errors}`);
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

  private updateSubscription(): void {
    const request: UpdateSubscriptionRequestDTO = {
      messageType: 'update_subscription',
      subscriptionId: this.getSubscriptionId(),
      parameters: {
        frequency: `${this.updateFrequency}`,
        filter: this.filterRegex,
        page: `${this.page}`,
        pageSize: `${this.pageSize}`
      }
    };

    this.hazelcastService.sendUpdateSubscription(request).then((response: UpdateSubscriptionResponseDTO) => {
      if (!!response.error) {
        this.snackbarService.show(`Error while updating the subscription: ${response.error}`);

        // When an error occours, the BE sends the current value
        this.updateFrequency = parseInt(response.parameters['frequency'], 10);
        this.page = parseInt(response.parameters['page'], 10);
        this.pageSize = parseInt(response.parameters['pageSize'], 10);
      } else {
        this.snackbarService.show('Subscription updated');
      }
    }).catch((error: ErrorMessageDTO) => {
      this.snackbarService.show(`Error while updating the subscription: ${error.errors.join('\n')}`);
    });
  }

  public get clusterName(): string {
    return this.clustersService.getCurrentCluster().instanceName;
  }

  public tabCreated(tab: TabData): void {
  }

  public getSubscriptionId(): number {
    if (!!this.dataSub && !!this.dataSub['subscribeResponse']) {
      return (this.dataSub['subscribeResponse'] as SubscribeResponseDTO).subscriptionId;
    }

    return undefined;
  }

  public handleKeydown(event: KeyboardEvent): void {
    if (event.ctrlKey && event.which === 13) {
      this.updateSubscription();
    }
  }
}
