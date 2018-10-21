import {Component, Input, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {MapProductDTO} from '@shared/dto/topic-products.dto';
import {
  ErrorMessageDTO, SubscribeResponseDTO, SubscriptionNoticeResponseDTO,
  UpdateSubscriptionRequestDTO, UpdateSubscriptionResponseDTO
} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';

@Component({
  templateUrl: './page-dashboard-map.component.html',
  styleUrls: [ './page-dashboard-map.component.scss' ]
})
export class PageDashboardMapComponent implements TabAwareComponent, OnDestroy {
  // Name of the map
  @Input()
  public mapName: string;

  // Data subscription
  private dataSub: Subscription;

  // Current data
  public data: MapProductDTO = undefined;

  // Update frequency
  public updateFrequency: number = 1;

  // Pagination
  public page: number = 1;
  public pageSize: number = 15;

  // Filtering and slicing
  public filterScript: string = 'true';
  public sliceScript: string = '$..*';

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
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

  public trackUpdateFrequencyChange(frequency: number): void {
    this.updateFrequency = frequency;
    this.updateSubscription();
  }

  public pull(): void {
    this.hazelcastService.sendPullSubscription(this.getSubscriptionId());
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      const parameters: any = {
        frequency: `${this.updateFrequency}`,
        filter: this.filterScript,
        jsonPath: this.sliceScript,
        page: `${this.page}`,
        pageSize: `${this.pageSize}`
      };

      this.dataSub = this.hazelcastService.subscribeToMap(this.clustersService.getCurrentCluster().instanceName, this.mapName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<MapProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the map: ${error.errors}`);
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

  public tabCreated(tab: TabData): void {
  }

  private updateSubscription(): void {
    const request: UpdateSubscriptionRequestDTO = {
      messageType: 'update_subscription',
      subscriptionId: this.getSubscriptionId(),
      parameters: {
        frequency: `${this.updateFrequency}`,
        filter: this.filterScript,
        jsonPath: this.sliceScript,
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

      // In manual mode, always pull immediately
      if (this.isManual()) {
        this.pull();
      }
    }).catch((error: ErrorMessageDTO) => {
      this.snackbarService.show(`Error while updating the subscription: ${error.errors.join('\n')}`);
    });
  }

  public isManual(): boolean {
    return this.updateFrequency === 0;
  }

  public getSubscriptionId(): number {
    if (!!this.dataSub && !!this.dataSub['subscribeResponse']) {
      return (this.dataSub['subscribeResponse'] as SubscribeResponseDTO).subscriptionId;
    }

    return undefined;
  }
}
