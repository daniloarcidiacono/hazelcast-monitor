import {Component, Input, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {ListProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';

@Component({
  templateUrl: './page-dashboard-list.component.html',
  styleUrls: [ './page-dashboard-list.component.scss' ]
})
export class PageDashboardListComponent implements TabAwareComponent, OnDestroy {
  @Input()
  public listName: string;
  public data: ListProductDTO = undefined;
  private dataSub: Subscription;
  public subscriptionId: number;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToList(this.clustersService.getCurrentCluster().instanceName, this.listName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<ListProductDTO>) => {
          this.data = notice.notice;
          this.subscriptionId = notice.subscriptionId;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the list: ${error.errors}`);
        }
      );
    }
  }

  public beforeHide(): void {
    if (!!this.dataSub) {
      this.dataSub.unsubscribe();
      this.dataSub = undefined;
      this.subscriptionId = undefined;
    }
  }

  public tabCreated(tab: TabData): void {
  }
}
