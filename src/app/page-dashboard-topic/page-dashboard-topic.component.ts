import {Component, Input, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {TopicProductDTO} from '@shared/dto/topic-products.dto';
import {
  ErrorMessageDTO, SubscribeResponseDTO, SubscriptionNoticeResponseDTO,
  UpdateSubscriptionRequestDTO, UpdateSubscriptionResponseDTO
} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';

@Component({
  templateUrl: './page-dashboard-topic.component.html',
  styleUrls: [ './page-dashboard-topic.component.scss' ]
})
export class PageDashboardTopicComponent implements TabAwareComponent, OnDestroy {
  // Name of the topic
  @Input()
  public topicName: string;

  // Data subscription
  private dataSub: Subscription;

  // Tab reference
  private tab: TabData;

  // Current data
  public data: TopicProductDTO[] = [];

  // Pagination
  private cap: number = 15;

  // Filtering and slicing
  public filterScript: string = 'true';
  public sliceScript: string = '$..*';

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
  }

  public tabCreated(tab: TabData): void {
    this.tab = tab;
    this.subscribe();
  }

  public isRecording(): boolean {
    return !!this.dataSub;
  }

  public record(): void {
    this.subscribe();
  }

  public stop(): void {
    this.unsubscribe();
  }

  public clear(): void {
    this.data = [];
  }

  public ngOnDestroy(): void {
    this.unsubscribe();
  }

  public beforeShow(): void {
  }

  public beforeHide(): void {
  }

  private subscribe(): void {
    this.tab.recording = true;

    if (!this.dataSub) {
      const parameters: any = {
        filter: this.filterScript,
        jsonPath: this.sliceScript
      };

      this.dataSub = this.hazelcastService.subscribeToTopic(this.clustersService.getCurrentCluster().instanceName, this.topicName, parameters).subscribe(
        (notice: SubscriptionNoticeResponseDTO<TopicProductDTO>) => {
          this.data.push(notice.notice);
          while (this.data.length > this.cap) {
            this.data.shift();
          }
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the topic: ${error.errors}`);
        }
      );
    }
  }

  private unsubscribe(): void {
    this.tab.recording = false;
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
        filter: this.filterScript,
        jsonPath: this.sliceScript
      }
    };

    this.hazelcastService.sendUpdateSubscription(request).then((response: UpdateSubscriptionResponseDTO) => {
      if (!!response.error) {
        this.snackbarService.show(`Error while updating the subscription: ${response.error}`);
      } else {
        this.snackbarService.show('Subscription updated');
      }
    }).catch((error: ErrorMessageDTO) => {
      this.snackbarService.show(`Error while updating the subscription: ${error.errors.join('\n')}`);
    });
  }

  public getSubscriptionId(): number {
    if (!!this.dataSub && !!this.dataSub['subscribeResponse']) {
      return (this.dataSub['subscribeResponse'] as SubscribeResponseDTO).subscriptionId;
    }

    return undefined;
  }
}
