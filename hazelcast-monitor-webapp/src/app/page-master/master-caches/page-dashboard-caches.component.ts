import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {CachesProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';
import {CacheSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardCacheStatsComponent} from '../../page-dashboard-cache-stats/page-dashboard-cache-stats.component';
import {PageDashboardCacheComponent} from '../../page-dashboard-cache/page-dashboard-cache.component';
import {SharedTabsService} from '@shared/services/shared-tabs.service';

@Component({
  templateUrl: './page-dashboard-caches.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardCachesComponent extends SharedMasterComponent<CachesProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    super(DistributedObjectType.CACHE, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Caches of ${this.clusterName}`;
  }

  public navigateToStats(row: CacheSummaryDTO): void {
    const cacheName: string = row.name;

    this.tabsService.addTab({
      label: `${cacheName} statistics`,
      icon: SharedPageIconsConstants.STATS_ICON,
      componentClass: PageDashboardCacheStatsComponent,
      componentInputs: {
        cacheName: cacheName
      }
    });
  }

  public navigateToDetails(row: CacheSummaryDTO): void {
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
}
