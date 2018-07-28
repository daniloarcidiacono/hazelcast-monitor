import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {MapsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs/index';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {MapsTableModel} from './page-dashboard-maps.model';
import {TabAwareComponent} from "@shared/components/dynamic-tabs/shared-dynamic-tabs.model";
import {SharedTabsService} from "@shared/services/shared-tabs.service";
import {PageDashboardMapComponent} from "../page-dashboard-map/page-dashboard-map.component";

@Component({
  templateUrl: './page-dashboard-maps.component.html',
  styleUrls: [ './page-dashboard-maps.component.scss' ]
})
export class PageDashboardMapsComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: MapsProductDTO = undefined;
  public tableModel: MapsTableModel = new MapsTableModel();

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public navigateToMapDetails(row: number): void {
    const mapName: string = this.tableModel.maps.maps[row].name;

    this.tabsService.addTab({
      label: `${mapName} map`,
      componentClass: PageDashboardMapComponent,
      componentInputs: {
        mapName: mapName
      }
    });
  }

  public get clusterName(): string {
    return this.clustersService.getCurrentCluster().instanceName;
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToMaps(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<MapsProductDTO>) => {
          this.data = notice.notice;
          this.tableModel.maps = this.data;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the maps: ${error.errors}`);
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
}
