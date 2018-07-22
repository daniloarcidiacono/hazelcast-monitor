import {Component, HostBinding, OnDestroy} from '@angular/core';
import {ConnectionState, SharedWebSocketService} from "../shared/services/shared-websocket.service";
import {Router} from "@angular/router";
import {SharedHazelcastAgentService} from "@shared/services/shared-hazelcast-agent.service";
import {SharedSnackbarService} from "@shared/services/shared-snackbar.service";
import {SharedClustersService} from "@shared/services/shared-clusters.service";
import {Subscription} from "rxjs/index";
import {ErrorMessageDTO, SubscriptionNoticeResponseDTO} from "@shared/dto/hazelcast-monitor.dto";
import {MembersProductDTO, StatisticsProductDTO} from "@shared/dto/topic-products.dto";

@Component({
  templateUrl: './page-dashboard.component.html',
  styleUrls: ['./page-dashboard.component.scss']
})
export class PageDashboardComponent implements OnDestroy {
  @HostBinding('class')
  private classes: string = 'Page__Bottom';
  private wsStateSub: Subscription;
  private statsSub: Subscription;
  private currentStats: StatisticsProductDTO = undefined;

  public constructor(private clustersService: SharedClustersService,
                     private snackbarService: SharedSnackbarService,
                     private wsService: SharedWebSocketService,
                     private hazelcastService: SharedHazelcastAgentService,
                     private router: Router) {
    this.wsStateSub = this.wsService.onConnectivityChanged.subscribe((value: ConnectionState) => {
      if (value !== ConnectionState.CONNECTED) {
        this.snackbarService.show(`Connection lost with ${this.wsService.getAddress()}`);
        this.router.navigateByUrl('/connect');
      }
    });

    this.statsSub = this.hazelcastService.subscribeToStatistics(this.clustersService.getCurrentCluster().instanceName).subscribe(
      (notice: SubscriptionNoticeResponseDTO<StatisticsProductDTO>) => {
        this.currentStats = notice.notice;
      },
      (error: ErrorMessageDTO) => {
        this.snackbarService.show(`Could not fetch the statistics: ${error.errors}`);
      }
    );
  }

  public ngOnDestroy(): void {
    this.statsSub.unsubscribe();
    this.wsStateSub.unsubscribe();
  }
}
