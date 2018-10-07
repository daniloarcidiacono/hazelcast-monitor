import {Component, OnDestroy} from '@angular/core';
import {TopicsProductDTO, TopicSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {PageDashboardTopicComponent} from '../page-dashboard-topic/page-dashboard-topic.component';
import {SharedPageIconsConstants} from "@shared/constants/shared-page-icons.constants";

@Component({
  templateUrl: './page-dashboard-topics.component.html',
  styleUrls: [ './page-dashboard-topics.component.scss' ]
})
export class PageDashboardTopicsComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: TopicsProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public navigateToTopicDetails(row: TopicSummaryDTO): void {
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

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToTopics(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<TopicsProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the topics: ${error.errors}`);
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
