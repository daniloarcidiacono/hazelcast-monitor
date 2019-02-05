import {Component} from '@angular/core';
import {TopicsProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {TopicSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {PageDashboardTopicComponent} from '../../page-dashboard-topic/page-dashboard-topic.component';
import {SharedPageIconsConstants} from '@shared/constants/shared-page-icons.constants';
import {PageDashboardTopicStatsComponent} from '../../page-dashboard-topic-stats/page-dashboard-topic-stats.component';
import {SharedMasterComponent} from '../shared-master.component';
import {DistributedObjectType} from '@shared/dto/topics.dto';

@Component({
  templateUrl: './page-dashboard-topics.component.html',
  styleUrls: [ '../shared-master.component.scss' ]
})
export class PageDashboardTopicsComponent extends SharedMasterComponent<TopicsProductDTO> {
  public constructor(clustersService: SharedClustersService,
                     snackbarService: SharedSnackbarService,
                     hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    super(DistributedObjectType.TOPIC, clustersService, snackbarService, hazelcastService);
  }

  get title(): string {
    return `Topics of ${this.clusterName}`;
  }

  public navigateToStats(row: TopicSummaryDTO): void {
    const topicName: string = row.name;

    this.tabsService.addTab({
      label: `${topicName} statistics`,
      icon: SharedPageIconsConstants.STATS_ICON,
      componentClass: PageDashboardTopicStatsComponent,
      componentInputs: {
        topicName: topicName
      }
    });
  }

  public navigateToDetails(row: TopicSummaryDTO): void {
    const topicName: string = row.name;

    this.tabsService.addTab({
      label: topicName,
      icon: SharedPageIconsConstants.TOPICS_ICON,
      componentClass: PageDashboardTopicComponent,
      componentInputs: {
        topicName: topicName
      }
    });
  }
}
