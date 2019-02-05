import {Component} from '@angular/core';
import {SharedMasterComponent} from '../shared-master.component';
import {QueuesProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {DistributedObjectType} from '@shared/dto/topics.dto';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {QueueSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardQueueStatsComponent} from '../../page-dashboard-queue-stats/page-dashboard-queue-stats.component';
import {PageDashboardQueueComponent} from '../../page-dashboard-queue/page-dashboard-queue.component';

@Component({
  templateUrl: './page-dashboard-queues.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardQueuesComponent extends SharedMasterComponent<QueuesProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    super(DistributedObjectType.QUEUE, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Queues of ${this.clusterName}`;
  }

  public navigateToStats(row: QueueSummaryDTO): void {
    const queueName: string = row.name;

    this.tabsService.addTab({
      label: `${queueName} statistics`,
      icon: SharedPageIconsConstants.STATS_ICON,
      componentClass: PageDashboardQueueStatsComponent,
      componentInputs: {
        queueName: queueName
      }
    });
  }

  public navigateToDetails(row: QueueSummaryDTO): void {
    const queueName: string = row.name;

    this.tabsService.addTab({
      label: queueName,
      icon: SharedPageIconsConstants.QUEUES_ICON,
      componentClass: PageDashboardQueueComponent,
      componentInputs: {
        queueName: queueName
      }
    });
  }
}
