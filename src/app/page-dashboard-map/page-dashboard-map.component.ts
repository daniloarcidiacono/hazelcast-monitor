import {Component, Input, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {SharedClustersService} from '@shared/services/shared-clusters.service';
import {Subscription} from 'rxjs/index';
import {MapProductDTO} from '@shared/dto/topic-products.dto';
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from '@shared/dto/hazelcast-monitor.dto';
import {TabAwareComponent} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';

@Component({
  templateUrl: './page-dashboard-map.component.html',
  styleUrls: [ './page-dashboard-map.component.scss' ]
})
export class PageDashboardMapComponent implements TabAwareComponent, OnDestroy {
  @Input()
  public mapName: string;
  public data: MapProductDTO = undefined;
  private dataSub: Subscription;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public beforeShow(): void {
    if (!this.dataSub) {
      this.dataSub = this.hazelcastService.subscribeToMap(this.clustersService.getCurrentCluster().instanceName, this.mapName).subscribe(
        (notice: SubscriptionNoticeResponseDTO<MapProductDTO>) => {
          if (!this.data) {
            console.log("replacing");
            this.data = notice.notice;
          } else {
            console.log("Updating");
            this.data.entries.length = 0; //.clear();
            this.data.entries.push(...notice.notice.entries);
          }
        },
        (error: ErrorMessageDTO) => {
          this.snackbarService.show(`Could not fetch the map: ${error.errors}`);
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

  public isComplex(object: any): boolean {
    return typeof object === 'object';
  }
}
