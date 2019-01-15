import {Component, OnDestroy} from '@angular/core';
import {RingbuffersProductDTO} from '@shared/dto/topic-products-aliases.dto';
import {RingbufferSummaryDTO} from '@shared/dto/topic-products.dto';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedTabsService} from '@shared/services/shared-tabs.service';
import {Subscription} from 'rxjs/index';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {MdcDialog} from '@angular-mdc/web';
import {PageDashboardRingbuffersDialogComponent} from './page-dashboard-ringbuffers-dialog.component';

@Component({
  templateUrl: './page-dashboard-ringbuffers.component.html',
  styleUrls: [ './page-dashboard-ringbuffers.component.scss' ]
})
export class PageDashboardRingbuffersComponent implements TabAwareComponent, OnDestroy {
  private dataSub: Subscription;
  private data: RingbuffersProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private dialog: MdcDialog,
                     private tabsService: SharedTabsService) {
    this.beforeShow();
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public navigateToRingbufferDetails(row: RingbufferSummaryDTO): void {
    // @TODO: Show an alert
    const ringbufferName: string = row.name;
    this.dialog.open(PageDashboardRingbuffersDialogComponent, {
      data: {
        ringbufferName: ringbufferName
      },
      escapeToClose: true,
      clickOutsideToClose: true
    });
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToRingbuffers(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<RingbuffersProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the ringbuffers: ${error.errors}`);
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
