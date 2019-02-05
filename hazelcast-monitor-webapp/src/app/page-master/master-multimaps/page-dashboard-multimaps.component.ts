import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {MultiMapsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';
import {MapSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardMapStatsComponent} from '../../page-dashboard-map-stats/page-dashboard-map-stats.component';
import {PageDashboardMultiMapComponent} from '../../page-dashboard-multimap/page-dashboard-multimap.component';
import {SharedTabsService} from "@shared/services/shared-tabs.service";

@Component({
  templateUrl: './page-dashboard-multimaps.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardMultiMapsComponent extends SharedMasterComponent<MultiMapsProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    super(DistributedObjectType.MULTIMAP, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Multimaps of ${this.clusterName}`;
  }

  public navigateToStats(row: MapSummaryDTO): void {
    const mapName: string = row.name;

    this.tabsService.addTab({
      label: `${mapName} statistics`,
      icon: SharedPageIconsConstants.STATS_ICON,
      componentClass: PageDashboardMapStatsComponent,
      componentInputs: {
        mapName: mapName,
        mapType: DistributedObjectType.MAP
      }
    });
  }

  public navigateToDetails(row: MapSummaryDTO): void {
    const mapName: string = row.name;

    this.tabsService.addTab({
      label: mapName,
      icon: SharedPageIconsConstants.MAPS_ICON,
      componentClass: PageDashboardMultiMapComponent,
      componentInputs: {
        mapName: mapName
      }
    });
  }
}
