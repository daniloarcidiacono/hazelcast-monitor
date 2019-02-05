import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {ExecutorsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';
import {ExecutorSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardExecutorStatsComponent} from '../../page-dashboard-executor-stats/page-dashboard-executor-stats.component';
import {SharedTabsService} from "@shared/services/shared-tabs.service";

@Component({
  templateUrl: './page-dashboard-executors.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardExecutorsComponent extends SharedMasterComponent<ExecutorsProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    super(DistributedObjectType.EXECUTOR, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Executors of ${this.clusterName}`;
  }

  public navigateToStats(row: ExecutorSummaryDTO): void {
    const executorName: string = row.name;

    this.tabsService.addTab({
      label: `${executorName} statistics`,
      icon: SharedPageIconsConstants.STATS_ICON,
      componentClass: PageDashboardExecutorStatsComponent,
      componentInputs: {
        executorName: executorName
      }
    });
  }
}
