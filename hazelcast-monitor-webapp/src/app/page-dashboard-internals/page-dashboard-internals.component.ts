import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {Subscription} from 'rxjs/index';
import {InternalsProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {SharedClustersService} from "@shared/services/shared-clusters.service";

@Component({
  templateUrl: './page-dashboard-internals.component.html',
  styleUrls: [ './page-dashboard-internals.component.scss' ]
})
export class PageDashboardInternalsComponent implements TabAwareComponent, OnDestroy {
  public data: InternalsProductDTO = undefined;
  private dataSub: Subscription;

  public options: any = {
    maxLines: 1000,
    fontSize: 18,
    maxPixelHeight: 400,
    printMargin: false
  };

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
  }


  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToInternals(this.clustersService.getCurrentCluster().instanceName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<InternalsProductDTO>) => {
          this.data = notice.notice;
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the internals: ${error.errors}`);
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

  public tabCreated(tab: TabData): void {
  }
}
