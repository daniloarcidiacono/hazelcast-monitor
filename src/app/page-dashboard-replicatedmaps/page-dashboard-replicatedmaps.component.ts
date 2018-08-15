import {Component, OnDestroy} from '@angular/core';
import {
  MapsProductDTO, MapSummaryDTO, ReplicatedMapsProductDTO,
  ReplicatedMapSummaryDTO
} from '@shared/dto/topic-products.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {PageDashboardReplicatedMapComponent} from '../page-dashboard-replicatedmap/page-dashboard-replicatedmap.component';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';

@Component({
  templateUrl: './page-dashboard-replicatedmaps.component.html',
  styleUrls: [ './page-dashboard-replicatedmaps.component.scss' ]
})
export class PageDashboardReplicatedMapsComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: MapsProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public navigateToReplicatedMapDetails(row: ReplicatedMapSummaryDTO): void {
    const mapName: string = row.name;

    this.tabsService.addTab({
      label: `${mapName} map`,
      componentClass: PageDashboardReplicatedMapComponent,
      componentInputs: {
        mapName: mapName
      }
    });
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToReplicatedMaps(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<ReplicatedMapsProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the replicated maps: ${error.errors}`);
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
